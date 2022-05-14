package com.teradata.openapi.master.schedule.dao

import java.io.StringWriter

import com.teradata.openapi.framework.OpenApiLogging
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import com.codahale.jerkson.Json._
import com.teradata.openapi.framework.deploy.{ArgsNecy, RepArg, ReqArg, SorcType}
import com.teradata.openapi.framework.model.ApiRssInfoRow
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.deploy.dao.ApiInfoDao

import scala.collection.mutable.ListBuffer


/**
  * Created by John on 2016/5/12.
  */
class ApiRssInfoDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_rss_info"

  class ApiRssInfoTable(tag: Tag) extends Table[ApiRssInfoRow](tag, Option(schemaName), tableName) {
    def rss_id = column[Int]("rss_id", O.AutoInc)

    def rss_start_time = column[Long]("rss_start_time")

    def rss_end_time = column[Long]("rss_end_time")

    def app_key = column[String]("appkey")

    def api_id = column[Int]("api_id")

    def api_version = column[Int]("api_version")

    def form_code = column[String]("form_code")

    def encode = column[String]("encode")

    def req_arg = column[String]("req_arg")

    def req_arg_render = column[String]("req_arg_render")

    def respn_arg = column[String]("respn_arg")

    def respn_arg_render = column[String]("respn_arg_render")

    def push_arg = column[String]("push_arg")

    def push_arg_render = column[String]("push_arg_render")

    def retn_form_finger = column[String]("retn_form_finger")

    def retn_data_finger = column[String]("retn_data_finger")

    def trigger_methd = column[Int]("trigger_methd")

    def trigger_sorc = column[String]("trigger_sorc")

    def priority = column[Int]("priority")

    def eff_flag = column[Int]("eff_flag")

    def idx = index("idx_rss", rss_id , unique = true)

    def * = (rss_id, rss_start_time, rss_end_time, app_key, api_id, api_version, form_code, encode, req_arg, req_arg_render, respn_arg, respn_arg_render, push_arg, push_arg_render, retn_form_finger, retn_data_finger, trigger_methd, trigger_sorc, priority, eff_flag) <> (ApiRssInfoRow.tupled, ApiRssInfoRow.unapply)
  }

  lazy val apirssinfo: TableQuery[ApiRssInfoTable] = TableQuery[ApiRssInfoTable]
  lazy val apiInfo = ApiInfoDao().apiInfo
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

  def loadByApiIds(apiIds: List[Int]) = {
    val sql = apirssinfo.join(apiInfo).on{case (rssinfo,apiinfo) => rssinfo.api_id === apiinfo.api_id && rssinfo.api_version === apiinfo.api_version}
      .filter{case (rssinfo,apiinfo) => rssinfo.api_id.inSet(apiIds)}
      .map{case (rssinfo,apiinfo) => (rssinfo.rss_id,rssinfo.rss_start_time,rssinfo.rss_end_time,rssinfo.app_key,rssinfo.api_id,rssinfo.api_version,apiinfo.api_name,apiinfo.api_sort,rssinfo.form_code,rssinfo.encode,rssinfo.req_arg,rssinfo.respn_arg,rssinfo.push_arg,rssinfo.retn_form_finger,rssinfo.retn_data_finger,rssinfo.trigger_methd,rssinfo.trigger_sorc,rssinfo.priority,rssinfo.eff_flag  )}
      .result
    val result = exec(sql)
    result
  }

  def loadByTimeTrigger: Seq[ApiRssInfoRow] = {
    val sql = apirssinfo.filter(_.trigger_methd === 1).sorted(_.priority.desc.nullsLast).result
    val result = exec(sql)
    result
  }


  def upsert(app_key: String, api_id: Int, api_version: Int, form_code: Int, encode: Int, req_arg: List[ReqArg],req_arg_render:String, respn_arg: List[RepArg],respn_arg_render :String, push_arg: String,push_arg_render:String, retn_form_finger: String, retn_data_finger: String, trigger_methd: Int, trigger_sorc: String, priority: Int, rss_id: Option[Int] = None) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val currTime = System.currentTimeMillis()
    val req_arg_json = generate(req_arg)
    val rep_arg_json = generate(respn_arg)
    val insert = s"INSERT INTO $ModulesAffectedTableName (rss_start_time ,rss_end_time ,app_key ,api_id ,api_version ,form_code ,encode ,req_arg ,req_arg_render ,respn_arg ,respn_arg_render ,push_arg,push_arg_render ,retn_form_finger ,retn_data_finger ,trigger_methd ,trigger_sorc ,priority) " +
      s"SELECT $currTime,$currTime,'$app_key','$api_id',$api_version,$form_code,$encode,'$req_arg_json','$req_arg_render','$rep_arg_json','$respn_arg_render','$push_arg','$push_arg_render','$retn_form_finger','$retn_data_finger',$trigger_methd,'$trigger_sorc',$priority;"
    val upsert = s"UPDATE $ModulesAffectedTableName SET rss_end_time = $currTime ,app_key = $app_key ,api_id =$api_id ,api_version = $api_version, form_code = $form_code,encode = $encode,req_arg = '$req_arg_json',req_arg_render = '$req_arg_render',respn_arg = '$rep_arg_json',respn_arg_render = '$respn_arg_render',push_arg = '$push_arg',push_arg_render = '$push_arg_render',retn_form_finger = '$retn_form_finger',retn_data_finger = '$retn_data_finger',trigger_methd = '$trigger_methd',trigger_sorc = '$trigger_sorc',priority = $priority" +
      s"WHERE rss_id=$rss_id"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }

  def caseTypeToJson() = {

  }
}

object ApiRssInfoDao {
  def apply() = new ApiRssInfoDao()
}

object ApiRssInfoDaoTest extends DicMapFunc {
  def main(args: Array[String]) {
    val m = ApiRssInfoDao()
    val aa = m.loadByApiIds(List(1001))
    for (elem <- aa) {
      println(elem)
    }
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

  }
}
