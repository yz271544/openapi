package com.teradata.openapi.master.finder.dao

/**
  * Created by John on 2016/5/10.
  */


import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{ApiInfoRow, StructApiArgRow}
import com.teradata.openapi.master.deploy.dao.ApiInfoDao
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class StructApiArgDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "struct_api_arg"
  class StructApiArgTable(tag: Tag) extends Table[StructApiArgRow](tag, Option(schemaName), tableName) {
    def api_id = column[Int]("api_id", NotNull)

    def api_version = column[Int]("api_version",NotNull)

    def source_id = column[Int]("source_id", NotNull)

    def tab_alias = column[String]("tab_alias")

    def schema_name = column[String]("schema_name")

    def tab_name = column[String]("tab_name")

    def field_name = column[String]("field_name")

    def field_alias = column[String]("field_alias")

    def field_eff_stat = column[Int]("field_eff_stat")

    def field_sorc_type = column[String]("field_sorc_type")

    def field_targt_type = column[String]("field_targt_type")

    def field_title = column[String]("field_title")

    def must_type = column[Int]("must_type")

    def must_one_grp_id = column[Int]("must_one_grp_id")

    def req_arg_id = column[Char]("req_arg_id")

    def req_arg_deflt_val = column[String]("req_arg_deflt_val")

    def respn_arg_id = column[Char]("respn_arg_id")

    def respn_arg_samp_val = column[String]("respn_arg_samp_val")

    def field_file_desc = column[String]("field_file_desc")

    def calc_princ_id = column[Int]("calc_princ_id")

    def value_range = column[String]("value_range")

    override def * = (api_id,api_version, source_id, tab_alias, schema_name, tab_name, field_name
      , field_alias, field_eff_stat, field_sorc_type, field_targt_type, field_title, must_type, must_one_grp_id
      , req_arg_id, req_arg_deflt_val, respn_arg_id, respn_arg_samp_val, field_file_desc, calc_princ_id, value_range) <>(StructApiArgRow.tupled, StructApiArgRow.unapply)
  }

  val openapiDb = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  lazy val structApiArg: TableQuery[StructApiArgTable] = TableQuery[StructApiArgTable]
  lazy val apiInfo = ApiInfoDao().apiInfo

  def loadInfoJoinStruct(apiName: String, field_alias: String): Tuple3.type = {
    val apiInfoAndStructApiArg = apiInfo.join(structApiArg).on{case (apinfo,struct) => apinfo.api_id === struct.api_id && apinfo.api_version === struct.api_version}
      .filter { case (apinfo, struct) => apinfo.api_name === apiName && struct.field_alias === field_alias }
      .map { case (apinfo, struct) => (struct.schema_name, struct.tab_name, struct.tab_alias) }.result
    val result = exec(apiInfoAndStructApiArg)
    val rst = Tuple3
    result.foreach {e => rst(e._1,e._2,e._3)}
    rst
  }


  def loadtest(apiName: String, field_alias: String): Iterable[String] = {
    val apiInfoAndStructApiArg = apiInfo.join(structApiArg).on{case (apinfo,struct) => apinfo.api_id === struct.api_id && apinfo.api_version === struct.api_version}
      .filter { case (apinfo, struct) => apinfo.api_name === apiName && struct.field_alias === field_alias }
      .map { case (apinfo, struct) => (struct.schema_name, struct.tab_name, struct.tab_alias) }.result.statements
    apiInfoAndStructApiArg
  }

  def loadApiInfo: Seq[ApiInfoRow] = {
    val apiInfoSeq = apiInfo.filter(_.api_cls_code === 2).withFilter(_.api_stat_code === 1).withFilter(_.exam_stat === 2).result
    val result = exec(apiInfoSeq)
    result
  }

  def loadStructApiArg: Seq[StructApiArgRow] = {
    val structApiArgSeq = structApiArg.result
    val result = exec(structApiArgSeq)
    result
  }

}


object StructApiArgDao {
  def apply() = {
    new StructApiArgDao()
  }
}

object StructApiArgDaoTest {
  def main(args: Array[String]) {
    val m = StructApiArgDao()
    val sql = m.loadtest("tb_rpt_bo_mon","BARGAIN_NUM")
    println("JOIN SQL:" + sql)
  }
}
