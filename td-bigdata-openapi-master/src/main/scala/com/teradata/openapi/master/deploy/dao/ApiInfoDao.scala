package com.teradata.openapi.master.deploy.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiInfoRow
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/7/18.
  */
class ApiInfoDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_info"
  class ApiInfoTable(tag: Tag) extends Table[ApiInfoRow](tag, Option(schemaName), tableName) {
    def api_id = column[Int]("api_id", NotNull)

    def api_version = column[Int]("api_version",NotNull)

    def api_cls_code = column[Int]("api_cls_code")

    def api_stat_code = column[Int]("api_stat_code")

    def api_sort = column[Int]("api_sort")

    def data_strct_type_code = column[Int]("data_strct_type_code", NotNull)

    def relse_type = column[Int]("relse_type")

    def api_name = column[String]("api_name")

    def api_desc = column[String]("api_desc")

    def data_cycle_type = column[Int]("data_cycle_type")

    def relse_persn = column[String]("relse_persn")

    def relse_time = column[String]("relse_time")

    def tab_scale_type = column[Int]("tab_scale_type")

    def exam_stat = column[Int]("exam_stat")

    def api_class_name = column[String]("api_class_name")

    def trigger_methd = column[Int]("trigger_methd")

    override def * = (api_id,api_version,api_cls_code, api_stat_code, api_sort, data_strct_type_code, relse_type, api_name, api_desc,
      data_cycle_type, relse_persn, relse_time, tab_scale_type,exam_stat,api_class_name,trigger_methd) <>(ApiInfoRow.tupled, ApiInfoRow.unapply)
  }
  val openapiDb = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)
  lazy val apiInfo: TableQuery[ApiInfoTable] = TableQuery[ApiInfoTable]

}

object ApiInfoDao {
  def apply() = {
    new ApiInfoDao()
  }
}