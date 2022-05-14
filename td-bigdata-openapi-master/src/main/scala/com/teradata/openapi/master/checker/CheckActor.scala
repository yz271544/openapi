package com.teradata.openapi.master.checker

import java.io.{PipedInputStream, PipedOutputStream}
import java.sql.{Connection, Driver, SQLException}
import java.util.Properties

import akka.actor.{Actor, ActorRef, Props}
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{MetaTableInfoCollectResult, WebSearchMetaTableInfoToChecker}
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.pluginUtil.SqlUtil
import com.teradata.openapi.master.resolver.dao.SourceInfoDao
import org.postgresql.copy.CopyManager
import org.postgresql.core.BaseConnection
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._


/**
  * Created by lzf on 2016/7/7.
  */
class CheckActor extends Actor with OpenApiLogging {

  var sourceInfo = (new SourceInfoDao).loadSourceInfoMap.values
  implicit val execution = context.system.dispatcher

  case object Check

  val intervalMin = 30

  var checkTask = context.system.scheduler.scheduleOnce(30 second, self, Check)

  val processSource = mutable.Map[Int, ActorRef]()

  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  var conn: Connection = _
  var copyOut: PipedOutputStream = _
  var copyActor: ActorRef = _
  var hasError = false

  log.info("init check actor.......")

  override def postStop(): Unit = {
    checkTask.cancel()
  }

  override def receive: Receive = {

    case m@WebSearchMetaTableInfoToChecker(ref, reqId, sourceId, schemaName, tabName) =>
      log.debug(s"rcv WebSearchMetaTableInfoToChecker $m")

      val result =
        try {
          val ret = fetchOne(sourceId, schemaName.toUpperCase, tabName.toUpperCase)
          log.debug(s"WebSearchMetaTableInfoToChecker ret: $ret")
          if (ret > 0) 0 else 1
        } catch {
          case e: Exception => log.debug("WebSearchMetaTableInfoToChecker error:", e)
            1
        }
      ref ! MetaTableInfoCollectResult(reqId, result)

    case Check =>
      log.debug("receive check...")

      processSource.clear()
      hasError = false
      conn = openapiDb.source.createConnection()
      try {
        conn.setClientInfo("ApplicationName", "checkActor")
      }
      catch {
        case e: Exception =>
      }
      before()

      copyOut = new PipedOutputStream()
      copyActor = context.actorOf(Props(classOf[CopyActor], this))
      copyActor ! StartCopy

      sourceInfo.foreach(info =>
        info.source_type_code match {
          case "01" =>
            val actor = context.actorOf(Props(classOf[TdInfoActor], info.source_id))
            actor ! Start(info)
            processSource += info.source_id -> actor
          case "02" =>
            val actor = context.actorOf(Props(classOf[AsterInfoActor], info.source_id))
            actor ! Start(info)
            processSource += info.source_id -> actor
          case "05" =>
            val actor = context.actorOf(Props(classOf[OracleInfoActor], info.source_id))
            actor ! Start(info)
            processSource += info.source_id -> actor
          case _ =>
        }
      )

    case InfoData(buffer) =>

      //log.debug("receive buffer")
      if (!hasError) copyOut.write(buffer)

    case DataEnd(id) =>

      log.debug("receive end id {}", id)
      processSource -= id

      if (processSource.isEmpty) {
        log.debug("complete....")
        closeCopyStream()

        if (!hasError) {
          copyActor ! EndCopy
          after()
          closeDbCon()

          val actor = context.actorOf(Props(classOf[VerifyActor], this))
          actor ! Verify

          checkTask = context.system.scheduler.scheduleOnce(intervalMin minute, self, Check)
        }

      }
    case DataError(id) =>

      hasError = true
      processSource -= id
      closeCopyStream()
      closeDbCon()

    case CopyError =>

      log.debug("rcv copy error")
      hasError = true
      processSource.values.foreach(this.context.stop)
      checkTask = context.system.scheduler.scheduleOnce(intervalMin minute, self, Check)

    case m@_ => log.debug("unknown msg {}", m)


  }

  def closeDbCon(): Unit = {
    if (conn != null && !conn.isClosed) conn.close()
  }

  def closeCopyStream(): Unit = {
    if (copyOut != null) {
      copyOut.close()
      copyOut = null
    }
  }

  def before(): Unit = {

    exeUpdateSql("DROP TABLE IF EXISTS opi.sorc_field_info_temp ")
    exeUpdateSql("CREATE TABLE opi.sorc_field_info_temp AS  SELECT * FROM opi.sorc_field_info WHERE 1=2")

  }

  def after(): Unit = {

    exeUpdateSql("DROP TABLE IF EXISTS opi.sorc_tab_info_temp ")
    exeUpdateSql("CREATE TABLE opi.sorc_tab_info_temp AS  SELECT * FROM opi.sorc_tab_info WHERE 1=2")
    exeUpdateSql("insert into opi.sorc_tab_info_temp  SELECT source_id,schema_name,tab_name FROM opi.sorc_field_info_temp group by 1,2,3")

    conn.setAutoCommit(false)
    try {

      exeUpdateSql("alter table opi.sorc_field_info rename to sorc_field_info_old")
      exeUpdateSql("alter table opi.sorc_field_info_temp rename to sorc_field_info")
      exeUpdateSql("alter table opi.sorc_tab_info rename to sorc_tab_info_old")
      exeUpdateSql("alter table opi.sorc_tab_info_temp rename to sorc_tab_info")

      exeUpdateSql("drop view opi.sorc_field_info_vw")
      exeUpdateSql("drop table opi.sorc_tab_info_old")
      exeUpdateSql("drop table opi.sorc_field_info_old")

      exeUpdateSql(
        """CREATE OR REPLACE VIEW opi.sorc_field_info_vw
          |    (
          |        source_id,
          |        schema_name,
          |        tab_name,
          |        field_name,
          |        field_title,
          |        sorc_field_type,
          |        sorc_form,
          |        sorc_field_len,
          |        sorc_total_digit,
          |        sorc_prec_digit,
          |        field_targt_type,
          |        pi_flag,
          |        ppi_flag,
          |        ppi_type
          |    ) AS
          |SELECT
          |    t1.source_id,
          |    t1.schema_name,
          |    t1.tab_name,
          |    t1.field_name,
          |    coalesce(t1.field_title,'no title'),
          |    t1.sorc_field_type,
          |    t1.sorc_form,
          |    coalesce(t1.sorc_field_len,0),
          |    coalesce(t1.sorc_total_digit,0),
          |    coalesce(t1.sorc_prec_digit,0),
          |    t3.field_targt_type,
          |    coalesce(t1.pi_flag,0),
          |    coalesce(t1.ppi_flag,0),
          |    coalesce(t1.ppi_type,'null')
          |FROM
          |    ((opi.sorc_field_info t1
          |LEFT JOIN
          |    opi.source_info t2
          |ON
          |    ((
          |            t1.source_id = t2.source_id)))
          |LEFT JOIN
          |    opi.data_type_trnsf t3
          |ON
          |    (((
          |                t2.source_type_code = t3.source_type_code)
          |        AND ((
          |                    t1.sorc_field_type)::text = (t3.field_sorc_type)::text))))""".stripMargin)


      conn.commit()
    }
    catch {
      case e: SQLException =>
        conn.rollback()
    }
    finally {
      conn.setAutoCommit(true)
    }

  }

  def exeUpdateSql(sql: String): Unit = {

    val ps = conn.prepareStatement(sql)
    ps.executeUpdate()
    ps.close()

  }

  def fetchOne(sourceId: Int, schemaName: String, tabName: String): Int = {

    val sourceOpt = sourceInfo.find(_.source_id == sourceId)
    if (sourceOpt.isEmpty) throw new Exception(s"unknown source id: $sourceId")

    val source = sourceOpt.get
    val sconn = SqlUtil.getConnection(source)
    var count = 0

    try {
      val sql =
        source.source_type_code match {
          case "01" => s"select upper(trim(t1.DatabaseName))\n,upper(trim(t1.TableName))\n,upper(trim(t1.ColumnName))\n,trim(t1.ColumnTitle)\n,t1.ColumnType\n,t1.ColumnFormat\n,t1.ColumnLength\n,t1.DecimalTotalDigits\n,t1.DecimalFractionalDigits\n,case when t1.columnname = t2.columnname then 1 else 0 end as pi_flag\n,case when INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0 then 1 else 0 end ppi_flag\n\n,'null'\n\n\nfrom dbc.columns t1\nleft join dbc.indices t2\non t1.databasename = t2.databasename\nand t1.tablename =t2.tablename\nand t1.ColumnName=t2.ColumnName\nleft join dbc.IndexConstraints t3\non t1.databasename = t3.databasename\nand t1.tablename =t3.tablename\nAND INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0\nwhere t1.tablename='$tabName' and t1.databasename='$schemaName' and t1.ColumnType is not null \norder by t1.ColumnId"
          case "02" => s"select upper(t3.schemaname) as schemaname \n,upper(t2.tablename) as tablename \n,upper(t1.colname) as colname \n,'no title' as coltitle \n,t4.typename \n,'no format' as colformat \n,t4.typelen \n,cast(substring(t1.coltype from E'(\\\\d+)') as integer) as total_digit \n,cast(substring(t1.coltype from E'\\\\d+,(\\\\d+)') as integer) as perm_digit \n,case when t1.ispartitionkey then 1 else 0 end\n,case when t1.colname=t5.partitionexpr then 1 else 0 end as ppi_flag \n,case when t1.colname=t5.partitionexpr then t5.format else null end as ppi_type \nfrom nc_user_owned_columns t1 \ninner join nc_user_owned_tables t2 \non t1.relid=t2.tableid \ninner join nc_user_schemas t3 \non t2.schemaid=t3.schemaid \ninner join nc_types t4 \non t1.typeid = t4.typeid \nleft join nc_user_owned_parent_partitions t5 \non t2.tableid=t5.tableid \nand t1.colname = t5.partitionexpr \nwhere  upper(t3.schemaname)='$schemaName' and upper(t2.tablename)='$tabName'  \norder by colnum"

          case "05" => s"select\nupper(OWNER)\n,upper(TABLE_NAME)\n,upper(COLUMN_NAME)\n,'' \n,DATA_TYPE \n,''\n,DATA_LENGTH\n,DATA_PRECISION\n,DATA_SCALE\n,0\n, 0\n,''\nfrom dba_tab_columns \nwhere  upper(OWNER)='$schemaName' and upper(TABLE_NAME)='$tabName'"
          case _ => throw new Exception(s"unknown support source: $source")
        }

      val ps = sconn.prepareStatement(sql)
      val rs = ps.executeQuery()

      val infos = ArrayBuffer[FieldInfoRow]()
      while (rs.next()) {

        val schema_name = rs.getString(1)
        val tab_name = rs.getString(2)
        val field_name = rs.getString(3)
        val field_title = rs.getString(4)
        val sorc_field_type = rs.getString(5)
        val sorc_form = rs.getString(6)
        val sorc_field_len = rs.getInt(7)
        val sorc_total_digit = rs.getInt(8)
        val sorc_prec_digit = rs.getInt(9)
        val pi_flag = rs.getInt(10)
        val ppi_flag = rs.getInt(11)
        val ppi_type = rs.getString(12)

        infos += FieldInfoRow(sourceId, schema_name, tab_name, field_name, field_title, sorc_field_type, sorc_form, sorc_field_len, sorc_total_digit, sorc_prec_digit, pi_flag, ppi_flag, ppi_type)
      }
      rs.close()
      ps.close()

      if (infos.isEmpty) {
        if(source.source_type_code=="01") {
          infos ++= getTdViewInfo(sourceId, sconn, schemaName, tabName)
        }
        else return 0
      }

      conn = openapiDb.source.createConnection()
      conn.setAutoCommit(false)
      val insertsql = "insert into opi.sorc_field_info values(?,?,?,?,?,?,?,?,?,?,?,?,?)"

      try {
        infos.foreach(info => {
          val ps = conn.prepareStatement(insertsql)
          ps.setInt(1, info.source_id)
          ps.setString(2, info.schema_name)
          ps.setString(3, info.tab_name)
          ps.setString(4, info.field_name.toUpperCase)
          ps.setString(5, info.field_title)
          ps.setString(6, info.sorc_field_type)
          ps.setString(7, info.sorc_form)
          ps.setInt(8, info.sorc_field_len)
          ps.setInt(9, info.sorc_total_digit)
          ps.setInt(10, info.sorc_prec_digit)
          ps.setInt(11, info.pi_flag)
          ps.setInt(12, info.ppi_flag)
          ps.setString(13, info.ppi_type)

          count += ps.executeUpdate()
          ps.close()

        })

        val instabs = s"insert into opi.sorc_tab_info values ($sourceId, '$schemaName', '$tabName')"
        val insps = conn.prepareStatement(instabs)
        insps.executeUpdate()
        insps.close()
        conn.commit()
      } catch {
        case e: Exception =>
          conn.rollback()
          throw e

      } finally {
        conn.close()
      }

    }
    finally {
      sconn.close()
    }
    count
  }

  private def getTdViewInfo(sourceId: Int, conn:Connection, schemaName: String, tabName: String):ArrayBuffer[FieldInfoRow]= {

    val infos = ArrayBuffer[FieldInfoRow]()
    val tmpTabName = "VT_TMP"

    val createTmpSql = s"CREATE VOLATILE MULTISET TABLE $tmpTabName AS (SELECT * FROM $schemaName.$tabName) WITH NO DATA"
    conn.prepareStatement(createTmpSql).executeUpdate()
    val helpSql = s"HELP TABLE $tmpTabName"
    val ps2 = conn.prepareStatement(helpSql)
    val rs2 = ps2.executeQuery()
    while(rs2.next()) {

      val field_name = rs2.getString(1)
      val field_title = rs2.getString(6)
      val sorc_field_type = rs2.getString(2)
      val sorc_form = rs2.getString(5)
      val sorc_field_len = rs2.getInt(7)
      val sorc_total_digit = rs2.getInt(8)
      val sorc_prec_digit = rs2.getInt(9)
      val pi_flag = 0
      val ppi_flag = 0
      val ppi_type = ""

      infos += FieldInfoRow(sourceId, schemaName, tabName, field_name, field_title, sorc_field_type, sorc_form, sorc_field_len, sorc_total_digit, sorc_prec_digit, pi_flag, ppi_flag, ppi_type)
    }
    val dropSql = s"DROP TABLE $tmpTabName"
    conn.prepareStatement(dropSql).executeUpdate()
    infos
  }

}


class CopyActor(check: CheckActor) extends Actor with OpenApiLogging {

  override def postStop(): Unit = {
    log.debug("copy actor stop....")
  }

  override def receive: Receive = {

    case StartCopy =>
      try {
        startCopy()
      } catch {
        case e: Exception =>
          log.error("copy error ", e)
          sender() ! CopyError
          context stop self
      }

    case EndCopy =>
      log.debug("end copy")
      context stop self
  }

  def startCopy(): Unit = {
    log.debug("start copy")
    val copySql = "COPY opi.sorc_field_info_temp FROM stdin WITH DELIMITER as '\1' NULL AS '\2'"

    val is = new PipedInputStream(check.copyOut)
    val copy = new CopyManager(check.conn.asInstanceOf[BaseConnection])
    copy.copyIn(copySql, is)
  }
}


class TdInfoActor(val id: Int) extends BaseInfoActor {

  //val sql = "select t1.DatabaseName\n,t1.TableName\n,t1.ColumnName\n,t1.ColumnType\n,t1.ColumnTitle\n,t1.ColumnType\n,t1.ColumnFormat\n,t1.ColumnLength\n,t1.DecimalTotalDigits\n,t1.DecimalFractionalDigits\n,'target_type'\n,case when t1.columnname = t2.columnname then 1 else 0 end as pi_flag\n,substr(ConstraintText,index(ConstraintText,'RANGE_N(')+CHARACTER_LENGTH('RANGE_N('),index(ConstraintText,'BETWEEN')-index(ConstraintText,'RANGE_N(')-CHARACTER_LENGTH('RANGE_N('))\n,case when INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0 then 1 else 0 end ppi_flag\n\n,substr(ConstraintText,INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))),INDEX(upper(t3.ConstraintText),)\n\n\nfrom dbc.columns t1\nleft join dbc.indices t2\non t1.databasename = t2.databasename\nand t1.tablename =t2.tablename\nand t1.ColumnName=t2.ColumnName\nleft join dbc.IndexConstraints t3\non t1.databasename = t3.databasename\nand t1.tablename =t3.tablename\nAND INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0\nwhere t1.databasename = 'RPTMART3'\nand t1.tablename = 'TB_RPT_YW002_MON'\norder by t1.ColumnId"

  val sql = "select upper(trim(t1.DatabaseName))\n,upper(trim(t1.TableName))\n,upper(trim(t1.ColumnName))\n,trim(t1.ColumnTitle)\n,t1.ColumnType\n,t1.ColumnFormat\n,t1.ColumnLength\n,t1.DecimalTotalDigits\n,t1.DecimalFractionalDigits\n,case when t1.columnname = t2.columnname then 1 else 0 end as pi_flag\n,case when INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0 then 1 else 0 end ppi_flag\n\n,'null'\n\n\nfrom dbc.columns t1\nleft join dbc.indices t2\non t1.databasename = t2.databasename\nand t1.tablename =t2.tablename\nand t1.ColumnName=t2.ColumnName\nleft join dbc.IndexConstraints t3\non t1.databasename = t3.databasename\nand t1.tablename =t3.tablename\nAND INDEX(upper(t3.ConstraintText), TRIM(upper(t1.Columnname))) > 0\nwhere 1=1 \norder by t1.ColumnId"

  val driver = "com.teradata.jdbc.TeraDriver"

  def url(ip: String, port: Int, db:String) = s"jdbc:teradata://$ip/DBS_PORT=$port,TMODE=TERA,CHARSET=ASCII,CLIENT_CHARSET=GBK"


}

class AsterInfoActor(val id: Int) extends BaseInfoActor {

  val sql = "select upper(t3.schemaname) as schemaname \n,upper(t2.tablename) as tablename \n,upper(t1.colname) as colname \n,'no title' as coltitle \n,t4.typename \n,'no format' as colformat \n,t4.typelen \n,cast(substring(t1.coltype from E'(\\\\d+)') as integer) as total_digit \n,cast(substring(t1.coltype from E'\\\\d+,(\\\\d+)') as integer) as perm_digit \n,case when t1.ispartitionkey then 1 else 0 end\n,case when t1.colname=t5.partitionexpr then 1 else 0 end as ppi_flag \n,case when t1.colname=t5.partitionexpr then t5.format else null end as ppi_type \nfrom nc_user_owned_columns t1 \ninner join nc_user_owned_tables t2 \non t1.relid=t2.tableid \ninner join nc_user_schemas t3 \non t2.schemaid=t3.schemaid \ninner join nc_types t4 \non t1.typeid = t4.typeid \nleft join nc_user_owned_parent_partitions t5 \non t2.tableid=t5.tableid \nand t1.colname = t5.partitionexpr \nwhere  1=1  \norder by colnum"
  val driver = "com.asterdata.ncluster.Driver"

  def url(ip: String, port: Int, db:String) = s"jdbc:ncluster://$ip:$port/beehive"

}

class OracleInfoActor(val id: Int) extends BaseInfoActor {

  val sql = "select\nupper(OWNER)\n,upper(TABLE_NAME)\n,upper(COLUMN_NAME)\n,'' \n,DATA_TYPE \n,''\n,DATA_LENGTH\n,DATA_PRECISION\n,DATA_SCALE\n,0\n, 0\n,''\nfrom dba_tab_columns \nwhere  1=1  \norder by COLUMN_ID"
  val driver = "oracle.jdbc.driver.OracleDriver"

  def url(ip: String, port: Int, db:String) = s"jdbc:oracle:thin:@$ip:$port:$db"

}

sealed trait BaseInfoActor extends Actor with OpenApiLogging {

  def id: Int

  def sql: String

  def driver: String

  def url(ip: String, port: Int, db:String): String

  var conn: Connection = _

  override def postStop(): Unit = {
    log.debug("{} stop....", this)
    if (conn != null) conn.close()
  }

  override def receive: Receive = {
    case Start(SourceInfoRow(_, _, _, _, ip, port, db, user_name, pwd, _, _, _, _, _, _, _)) =>

      try {
        process(ip, port, user_name, pwd, db, sender())
      } catch {
        case e: Exception =>
          log.debug("get data error.", e)
          sender() ! DataError(id)
          context stop self
      }

  }

  def process(ip: String, port: Int, userName: String, pwd: String, db:String, actor: ActorRef): Unit = {

    log.debug("start process .... ip {}", ip)

    val driverIns = Class.forName(driver).newInstance().asInstanceOf[Driver]
    val info = new Properties()
    info.put("user", userName)
    info.put("password", pwd)
    val jdbcUrl = url(ip, port, db)
    log.debug("url: {}", jdbcUrl)
    conn =
        try {
          driverIns.connect(jdbcUrl, info)
        }
        catch {
          case e: Exception =>
            throw e
        }

    //conn.setAutoCommit(false)
    val ps = conn.prepareStatement(sql)
    //ps.setFetchSize(1000)
    val rs = ps.executeQuery()
    val meta = rs.getMetaData
    val columns = meta.getColumnCount

    val buffer = new StringBuilder()

    while (rs.next()) {
      buffer.append(id).append(1.toChar)
      for (i <- 1 to columns) {
        val s = rs.getString(i)
        buffer.append(if (s != null) s.trim.replaceAll("(\r\n|\r|\n|\n\r)", "") else 2.toChar).append(if (i < columns) 1.toChar else '\n')
      }

      if (buffer.length > 1024 * 64) {
        actor ! InfoData(buffer.result().getBytes)
        buffer.clear()
      }

    }

    if (buffer.nonEmpty) {
      actor ! InfoData(buffer.result().getBytes)
    }

    actor ! DataEnd(id)
    context stop self

  }

}


class VerifyActor(check: CheckActor) extends Actor with OpenApiLogging {

  val sql =
    """select a.api_id, a.api_version, a.field_alias
      |from opi.struct_api_arg a
      |inner join opi.api_tab_info b
      |on a.api_id = b.api_id
      |and a.api_version = b.api_version
      |and a.field_alias = b.field_alias
      |left join opi.sorc_field_info c
      |on b.source_id = c.source_id
      |and b.schema_name = c.schema_name
      |and b.tab_name = c.tab_name
      |and b.field_name = c.field_name
      |
      |where a.field_eff_stat = 1
      |and (c.field_name is null or b.field_type<>c.sorc_field_type or b.field_len <> c.sorc_field_len or b.field_tot_digit <> c.sorc_total_digit or b.field_prec_digit<> sorc_prec_digit)""".stripMargin

  val updateSql = "update opi.struct_api_arg set field_eff_stat=0 where api_id=? and api_version=? and field_alias=?"

  val updateApiSql = "update opi.api_info set api_stat_code=0 where api_id=? and api_version=?"

  override def receive: Receive = {
    case Verify =>

      val conn = check.openapiDb.source.createConnection()
      try {
        conn.setClientInfo("ApplicationName", "VerifyActor")
      } catch {
        case e: Exception =>
      }

      try {
        verify(conn)
      } catch {
        case e: Exception => log.error("verify exception", e)
      }
      finally {
        context stop self
        conn.close()

      }

  }

  def verify(conn: Connection): Unit = {

    log.debug("start verify .....")
    val ps = conn.prepareStatement(sql)
    val rs = ps.executeQuery()

    val changed = mutable.Set[(Int, Int, String)]()
    var count = 0
    var updateCount = 0

    while (rs.next()) {

      count += 1

      val api_id = rs.getInt("api_id")
      val api_version = rs.getInt("api_version")
      val field_alias = rs.getString("field_alias")

      val it = (api_id, api_version, field_alias)
      changed += it

    }
    rs.close()
    ps.close()

    changed.foreach(it => {
      val ps = conn.prepareStatement(updateSql)
      ps.setInt(1, it._1)
      ps.setInt(2, it._2)
      ps.setString(3, it._3)
      updateCount += ps.executeUpdate()
      ps.close()
    })

    changed.map(it => (it._1, it._2)).foreach(it => {
      val ps = conn.prepareStatement(updateApiSql)
      ps.setInt(1, it._1)
      ps.setInt(2, it._2)
      ps.executeUpdate()
      ps.close()
    })

    log.debug("verify end, process count: {}, update {}", count, updateCount)

  }

}


case class Start(info: SourceInfoRow)

case class InfoData(buffer: Array[Byte])

case class DataEnd(id: Int)

case class DataError(id: Int)

case object StartCopy

case object EndCopy

case object CopyError

case object Verify

case class FieldInfoRow(source_id: Int, schema_name: String, tab_name: String, field_name: String, field_title: String, sorc_field_type: String, sorc_form: String, sorc_field_len: Int, sorc_total_digit: Int, sorc_prec_digit: Int, pi_flag: Int, ppi_flag: Int, ppi_type: String)