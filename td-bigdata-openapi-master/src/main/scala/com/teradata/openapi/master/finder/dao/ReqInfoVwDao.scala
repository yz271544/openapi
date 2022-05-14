package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ReqInfoVwRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.jdbc.GetResult
import slick.lifted.ProvenShape
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/5/19.
  */
class ReqInfoVwDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "req_info_vw"

  class ReqInfoTable(tag: Tag) extends Table[ReqInfoVwRow](tag, Option(schemaName), tableName) {
    def req_id = column[String]("req_id", NotNull)

    def req_stat = column[Int]("req_stat")

    def req_time = column[Long]("req_time")

    def end_time = column[Long]("end_time")

    def appkey = column[String]("appkey")

    def api_id = column[Int]("api_id")

    def api_version = column[Int]("api_version")

    def api_name = column[String]("api_name")

    def api_sort = column[Int]("api_sort")

    def api_cls_code = column[Int]("api_cls_code")

    def form_code = column[String]("form_code")

    def encode = column[String]("encode")

    def req_arg = column[String]("req_arg")

    def respn_arg = column[String]("respn_arg")

    def push_arg = column[String]("push_arg")

    def retn_form_finger = column[String]("retn_form_finger")

    def retn_data_finger = column[String]("retn_data_finger")

    def api_visit_methd = column[Int]("api_visit_methd")

    def trigger_sorc = column[String]("trigger_sorc")

    def finder_loc = column[String]("finder_loc")

    def priority = column[Int]("priority")

    override def * : ProvenShape[ReqInfoVwRow] = (req_id, req_stat, req_time, end_time, appkey, api_id, api_version, api_name, api_sort, api_cls_code, form_code, encode, req_arg, respn_arg, push_arg,
      retn_form_finger, retn_data_finger, api_visit_methd, trigger_sorc, finder_loc, priority) <>(ReqInfoVwRow.tupled, ReqInfoVwRow.unapply)
  }

  implicit val GetReqInfoVwRow = GetResult(r =>
    ReqInfoVwRow(req_id = r.<<,
      req_stat = r.<<,
      req_time = r.<<,
      end_time = r.<<,
      appkey = r.<<,
      api_id = r.<<,
      api_version = r.<<,
      api_name = r.<<,
      api_sort = r.<<,
      api_cls_code = r.<<,
      form_code = r.<<,
      encode = r.<<,
      req_arg = r.<<,
      respn_arg = r.<<,
      push_arg = r.<<,
      retn_form_finger = r.<<,
      retn_data_finger = r.<<,
      api_visit_methd = r.<<,
      trigger_sorc = r.<<,
      finder_loc = r.<<,
      priority = r.<<))

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

  def loadALL: Seq[ReqInfoVwRow] = {
    val sql = reqInfo.result
    val result = exec(sql)
    result
  }

  /*  def loadByTriggerSorc(apiIds:List[Int],api_visit_methd :Set[Int],reqStats :Set[Int]): Seq[ReqInfoVwRow] = {
      val sql = reqInfo.filter(_.api_id inSet apiIds).withFilter(_.req_stat inSet reqStats).withFilter(_.api_visit_methd inSet api_visit_methd).result
      val result = exec(sql)
      result
    }*/
  def loadByTriggerSorc(apiIdsVersions: List[(Int, Int)], api_visit_methd: Set[Int], reqStats: Set[Int]): Seq[ReqInfoVwRow] = {
    val apiVersionSetVal = apiIdsVersions.map(e => e._1 + "_" + e._2).mkString("'", "','", "'")
    val apiVisitMethdSetVal = api_visit_methd.mkString("'", "','", "'")
    val reqStatsSetVal = reqStats.mkString("'", "','", "'")
    val sql = sql"""
  select
  req_id,req_stat,req_time,end_time,appkey,api_id,api_version,api_name,api_sort,api_cls_code,form_code,encode,req_arg,respn_arg,push_arg,retn_form_finger,retn_data_finger,api_visit_methd,trigger_sorc,finder_loc,priority
  from
  #$schemaName.#$tableName
  where api_id||'_'||api_version in (#$apiVersionSetVal) and api_visit_methd in (#$apiVisitMethdSetVal) and req_stat in (#$reqStatsSetVal)""".as[ReqInfoVwRow]
    log.debug("loadByTriggerSorc SQL:" + sql.statements.toString)
    val result = exec(sql)
    result
  }

  def chkReqStat(reqId: String): Seq[(Int, String, String)] = {
    val sql = reqInfo.filter(_.req_id === reqId).map { columns => (columns.req_stat, columns.form_code, columns.retn_form_finger) }.result
    val sqlstatements = sql.statements
    log.debug("sqlstatements:" + sqlstatements)
    exec(sql)
  }

  def existsReq(api_visit_methd: Int, apiId: Int, appKey: String, finderLoc: String, reqStats: Set[Int]): Boolean = {
    val sql = reqInfo.filter(_.api_id === apiId).withFilter(_.appkey === appKey).withFilter(_.finder_loc === finderLoc).withFilter(_.req_stat inSet reqStats).withFilter(_.api_visit_methd === api_visit_methd).exists.result
    log.debug("existsReq SQL:" + sql.statements.toString)
    println("existsReq SQL:" + sql.statements.toString)
    exec(sql)
  }


}

object ReqInfoVwDao {
  def apply() = {
    new ReqInfoVwDao()
  }
}

object ReqInfoVwDaoTest {
  def main(args: Array[String]) {
    val m = ReqInfoVwDao()
    //m.upsert(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
  }
}
