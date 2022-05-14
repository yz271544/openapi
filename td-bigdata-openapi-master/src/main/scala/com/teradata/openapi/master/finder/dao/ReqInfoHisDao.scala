package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{ReqInfoHisRow, ReqInfoRow}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/5/19.
  */
class ReqInfoHisDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "req_info_his"
  val bakSchemaName = "opi"
  val bakTableName = "req_info"

  class ReqInfoHisTable(tag: Tag) extends Table[ReqInfoHisRow](tag, Option(schemaName), tableName) {
    def req_id = column[String]("req_id", NotNull)

    def req_stat = column[Int]("req_stat")

    def req_time = column[Long]("req_time")

    def end_time = column[Long]("end_time")

    def appkey = column[String]("appkey")

    def api_id = column[Int]("api_id")

    def api_version = column[Int]("api_version")

    def form_code = column[String]("form_code")

    def encode = column[String]("encode")

    def req_arg = column[String]("req_arg")

    def respn_arg = column[String]("respn_arg")

    def push_arg = column[String]("push_arg")

    def retn_form_finger = column[String]("retn_form_finger")

    def retn_data_finger = column[String]("retn_data_finger")

    def api_visit_methd = column[Int]("api_visit_methd")

    def trigger_methd = column[Int]("trigger_methd")

    def trigger_sorc = column[String]("trigger_sorc")

    def finder_loc = column[String]("finder_loc")

    def rss_id = column[Int]("rss_id")

    def priority = column[Int]("priority")

    def arch_time = column[Long]("arch_time")

    def arch_proc = column[String]("arch_proc")

    def * = (req_id, req_stat, req_time, end_time, appkey, api_id, api_version, form_code, encode, req_arg, respn_arg, push_arg,
      retn_form_finger, retn_data_finger, api_visit_methd, trigger_methd, trigger_sorc, finder_loc, rss_id, priority,arch_time,arch_proc) <>(ReqInfoHisRow.tupled, ReqInfoHisRow.unapply)
  }

  lazy val reqInfoHis: TableQuery[ReqInfoHisTable] = TableQuery[ReqInfoHisTable]
  //lazy val reqInfo: TableQuery[ReqInfoTable] = TableQuery[ReqInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = reqInfoHis.schema.create
    val sqlInfo = reqInfoHis.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = reqInfoHis.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def insertFromInfo(arch_proc:String, req_ids: List[String]): Int = {
    val reqIdFormat = req_ids.mkString("'","','","'")
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val HisTableName = s"$bakSchemaName.$bakTableName"
    val currTime = System.currentTimeMillis()
    val insert = s"INSERT INTO $ModulesAffectedTableName (req_id,req_stat,req_time,end_time,appkey,api_id,api_version,form_code,encode,req_arg,respn_arg,push_arg,retn_form_finger,retn_data_finger,api_visit_methd,trigger_methd,trigger_sorc,finder_loc,rss_id,priority,arch_time,arch_proc)" +
      s" SELECT req_id,req_stat,req_time,end_time,appkey,api_id,api_version,form_code,encode,req_arg,respn_arg,push_arg,retn_form_finger,retn_data_finger,api_visit_methd,trigger_methd,trigger_sorc,finder_loc,rss_id,priority,$currTime,'$arch_proc' FROM $HisTableName" +
      s" WHERE req_id in ($reqIdFormat)"
    log.debug("insertFromInfo:" + insert)
    exec(sqlu"#$insert")
  }

}

object ReqInfoHisDao {
  def apply() = {
    new ReqInfoHisDao()
  }
}

object ReqInfoHisDaoTest {
  def main(args: Array[String]) {
    val m = ReqInfoHisDao()
    //m.upsert(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
//    println(m.insertFromInfo("123", 2))
  }
}
