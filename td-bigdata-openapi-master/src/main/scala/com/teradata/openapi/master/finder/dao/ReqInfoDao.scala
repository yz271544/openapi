package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ReqInfoRow
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
class ReqInfoDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "req_info"

  class ReqInfoTable(tag: Tag) extends Table[ReqInfoRow](tag, Option(schemaName), tableName) {
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

    def * = (req_id, req_stat, req_time, end_time, appkey, api_id, api_version, form_code, encode, req_arg, respn_arg, push_arg,
      retn_form_finger, retn_data_finger, api_visit_methd, trigger_methd, trigger_sorc, finder_loc, rss_id, priority) <>(ReqInfoRow.tupled, ReqInfoRow.unapply)
  }

  lazy val reqInfo: TableQuery[ReqInfoTable] = TableQuery[ReqInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = reqInfo.schema.create
    val sqlInfo = reqInfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = reqInfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadALL: Seq[ReqInfoRow] = {
    val sql = reqInfo.result
    val result = exec(sql)
    result
  }

  def loadByTriggerSorc(apiIds: List[Int], api_visit_methd: Set[Int], reqStats: Set[Int]): Seq[ReqInfoRow] = {
    val sql = reqInfo.filter(_.api_id inSet apiIds).withFilter(_.req_stat inSet reqStats).withFilter(_.api_visit_methd inSet api_visit_methd).result
    val result = exec(sql)
    result
  }

  def existsReq(api_visit_methd: Int, apiId: Int, appKey: String, triggerSorc: String, reqStats: Set[Int]): Boolean = {
    val sql = reqInfo.filter(_.api_id === apiId).withFilter(_.appkey === appKey).withFilter(_.trigger_sorc === triggerSorc).withFilter(_.req_stat inSet reqStats).withFilter(_.api_visit_methd === api_visit_methd).exists.result
    exec(sql)
  }

  def updateStat(req_ids: List[String], req_stat: Int) = {
    val sql = reqInfo.filter(_.req_id inSet req_ids).map(_.req_stat)
    exec(sql.update(req_stat))
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    *
    * @return
    */
  def upsert(req_id: String, req_stat: Int, appkey: String, api_id: Int, api_version: Int, form_code: String, encode: String, req_arg: String, respn_arg: String, push_arg: String, retn_form_finger: String, retn_data_finger: String, api_visit_methd: Int, trigger_methd: Int = 0, trigger_sorc: String, finder_loc: String, rss_id: Int, priority: Int) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val currTime = System.currentTimeMillis()
    val insert = s"INSERT INTO $ModulesAffectedTableName (req_id,req_stat,req_time,end_time,appkey,api_id,api_version,form_code,encode,req_arg,respn_arg,push_arg,retn_form_finger,retn_data_finger,api_visit_methd,trigger_methd,trigger_sorc,finder_loc,rss_id,priority)" +
      s" SELECT '$req_id',$req_stat,$currTime,$currTime,'$appkey',$api_id,$api_version,'$form_code','$encode','$req_arg','$respn_arg','$push_arg','$retn_form_finger','$retn_data_finger',$api_visit_methd,$trigger_methd,'$trigger_sorc','$finder_loc',$rss_id,$priority"
    val upsert = s"UPDATE $ModulesAffectedTableName SET req_stat=$req_stat,end_time=$currTime WHERE req_id='$req_id'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }

  def updateReqStat(req_Id: String, req_stat: Int): Int = {
    val query = reqInfo.filter(_.req_id === req_Id).map(req => (req.req_stat, req.end_time))
    val action = query.update(req_stat, System.currentTimeMillis())
    exec(action)
  }

  def delete(reqId: String): Int = {
    exec(reqInfo.filter(_.req_id === reqId).delete)
  }

  def deleteRange(reqIds:List[String]): Int = {
    exec(reqInfo.filter(_.req_id inSet reqIds).delete)
  }

}

object ReqInfoDao {
  def apply() = {
    new ReqInfoDao()
  }
}

object ReqInfoDaoTest {
  def main(args: Array[String]) {
    val m = ReqInfoDao()
    //m.upsert(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
    println(m.updateReqStat("123", 2))
  }
}
