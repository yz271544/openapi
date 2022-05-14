package com.teradata.openapi.master.schedule.dao

import java.io.StringWriter

import com.teradata.openapi.framework.OpenApiLogging
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import com.teradata.openapi.framework.model.ApiRssInfoVwRow
import com.teradata.openapi.framework.util.DicMapFunc
import slick.jdbc.GetResult
import slick.profile.SqlStreamingAction


/**
  * Created by John on 2016/5/12.
  */
class ApiRssInfoVwDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_rss_info_vw"

  class ApiRssInfoView(tag: Tag) extends Table[ApiRssInfoVwRow](tag, Option(schemaName), tableName) {
    def rss_id = column[Int]("rss_id", O.AutoInc)

    def rss_start_time = column[Long]("rss_start_time",NotNull)

    def rss_end_time = column[Long]("rss_end_time",NotNull)

    def app_key = column[String]("appkey",NotNull)

    def api_id = column[Int]("api_id",NotNull)

    def api_version = column[Int]("api_version",NotNull)

    def api_name = column[String]("api_name",NotNull)

    def api_sort = column[Int]("api_sort",NotNull)

    def api_cls_code = column[Int]("api_cls_code",NotNull)

    def form_code = column[String]("form_code",NotNull)

    def encode = column[String]("encode",NotNull)

    def req_arg = column[String]("req_arg",NotNull)

    def respn_arg = column[String]("respn_arg",NotNull)

    def push_arg = column[String]("push_arg",NotNull)

    def retn_form_finger = column[String]("retn_form_finger",NotNull)

    def retn_data_finger = column[String]("retn_data_finger",NotNull)

    def trigger_methd = column[Int]("trigger_methd",NotNull)

    def trigger_sorc = column[String]("trigger_sorc",NotNull)

    def priority = column[Int]("priority",NotNull)

    def eff_flag = column[Int]("eff_flag",NotNull)

    def idx = index("idx_rss", rss_id, unique = false)

    def * = (rss_id, rss_start_time, rss_end_time, app_key, api_id, api_version, api_name, api_sort, api_cls_code, form_code, encode, req_arg, respn_arg, push_arg, retn_form_finger, retn_data_finger, trigger_methd, trigger_sorc, priority, eff_flag) <>(ApiRssInfoVwRow.tupled, ApiRssInfoVwRow.unapply)
  }

  implicit val GetApiRssInfoVwRow = GetResult(r =>
    ApiRssInfoVwRow(rss_id = r.<<,
      rss_start_time = r.<<,
      rss_end_time = r.<<,
      app_key = r.<<,
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
      trigger_methd = r.<<,
      trigger_sorc = r.<<,
      priority = r.<<,
      eff_flag = r.<<
    ) )
  lazy val apirssinfo: TableQuery[ApiRssInfoView] = TableQuery[ApiRssInfoView]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = apirssinfo.schema.create
    val sqlInfo = apirssinfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = apirssinfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

/*  def loadByApiIds(apiIds: List[Int]): Seq[ApiRssInfoVwRow] = {
    val sql = apirssinfo
      .filter(_.api_id inSet apiIds)
      .sorted(_.priority.desc)
      .result
    val result = exec(sql)
    result
  }*/


    def loadByApiIds(apiIdsVersions: List[(Int, Int)]): Seq[ApiRssInfoVwRow] = {
      val inSetVal: String = apiIdsVersions.map(e => e._1 + "_" + e._2).mkString("'","','","'")
      val sql: SqlStreamingAction[Vector[ApiRssInfoVwRow], ApiRssInfoVwRow, Effect] =  sql"""
  select
  rss_id,rss_start_time,rss_end_time,appkey,api_id,api_version,api_name,api_sort,api_cls_code,form_code,encode,req_arg,respn_arg,push_arg,retn_form_finger,retn_data_finger,trigger_methd,trigger_sorc,priority,eff_flag
  from
  #$schemaName.#$tableName
  where api_id||'_'||api_version in (#$inSetVal) and eff_flag='1'
  and extract(epoch from now())*1000::bigint >=rss_start_time
  and extract(epoch from now())*1000::bigint) < rss_end_time
  order by priority desc""".as[ApiRssInfoVwRow]
      log.debug("LoadTriggerTaskByApiIds and apiVersions inSetVal:" + inSetVal + " SQL:" + sql.statements.toString)
      //println("LoadTriggerTaskByApiIds and apiVersions inSetVal:" + inSetVal + " SQL:" + sql.statements.toString)
      val result = exec(sql)
    result
  }


  def loadByTimeTrigger: Seq[ApiRssInfoVwRow] = {
    val sql = apirssinfo.filter(_.trigger_methd === 2).sorted(_.priority.desc.nullsLast).result
    log.debug("loadByTimeTrigger SQL:" + sql.statements.toString)
    //println("loadByTimeTrigger SQL:" + sql.statements.toString)
    val result = exec(sql)
    result
  }

  def caseTypeToJson() = {

  }
}

object ApiRssInfoVwDao {
  def apply() = new ApiRssInfoVwDao()
}

object ApiRssInfoVwDaoTest extends DicMapFunc {
  def main(args: Array[String]) {
    val m = ApiRssInfoVwDao()
    //println("loadByTimeTriggertest SQL:" + m.loadByTimeTriggertest)
    //val aa = m.loadByApiIds(List(1001))
    /*for (elem <- aa) {
      println(elem)
    }*/
    //m.create()
    /*println("NECESSARY:" + ArgsNecy.NECESSARY.id + " getId:" + ArgsNecy.apply(0))
    println("MUSTONE:" + ArgsNecy.MUSTONE.id + " getId:" + ArgsNecy.apply(1))
    println("OPTION:" + ArgsNecy.OPTION.id + " getId:" + ArgsNecy.apply(2))
    println("withName:" + ArgsNecy.withName("NECESSARY"))
    println("with NO Name:" + ArgsNecy.withName("jah"))*/
    /*val c1_a = SorcType(1,"I","",4,0,0)
    val c1_b = SorcType(2,"DA","",4,0,0)
    val lco1 = List(c1_a,c1_b)

    val c2_a = SorcType(1,"I","",4,0,0)
    val c2_b = SorcType(2,"DA","",4,0,0)
    val lco2 = List(c2_a,c2_b)

    val r1 = ReqArg("deal_date","Int",lco1,1,List(201501,201502,201503),0,"pmid3","tb_mid_par_tot_user_mon","t1")
    val r2 = ReqArg("region_code","String",lco2,1,List("00","01","02","03","04","05","06","07","08","09","10","11"),0,"pmid3","tb_mid_par_tot_user_mon","t1")
    val req1 = List(r1,r2)

    var start = System.currentTimeMillis()
    val json = generate(req1)
    println((System.currentTimeMillis()-start)+"ms "+ json)
    start = System.currentTimeMillis()
    val ob = parse[List[ReqArg]](json)
    println((System.currentTimeMillis()-start)+"ms "+ ob)*/
    //println("loadByTimeTriggertest SQL:" + m.loadByTimeTriggertest)
    //println(m.loadByTimeTrigger)

    //println(m.loadByApiIdsStatement(List((1001,1),(1002,1),(2001,3))))
    val aa: Seq[ApiRssInfoVwRow] = m.loadByApiIds(List((1001,1),(1002,1),(2001,3)))
    for (elem <- aa) {
      println("Data:" + elem.rss_id + "|" + elem.api_id + "|" + elem.api_name)
    }

    val bb = m.loadByTimeTrigger
    for (elem <- bb) {
      println("TimeTrigger:" + elem.rss_id + "|" + elem.api_id + "|" + elem.api_name)
    }
  }
}
