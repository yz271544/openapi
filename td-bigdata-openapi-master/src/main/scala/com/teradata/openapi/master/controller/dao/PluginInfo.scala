package com.teradata.openapi.master.controller.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{PluginInfoRow, SourceInfoRow, StrategyCodeRow}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by lzf on 2016/5/12.
  */

class PluginInfoDao extends OpenApiLogging {

  class PluginInfoTable(tag: Tag) extends Table[PluginInfoRow](tag, Option("opi"), "plugin_info") {

    def source_type_code = column[String]("source_type_code", NotNull)
    def plugin_id = column[Int]("plugin_id", NotNull, O.AutoInc)
    def plugin_name = column[String]("plugin_name", NotNull)
    def plugin_desc = column[String]("plugin_desc", NotNull)
    def class_name = column[String]("class_name", NotNull)
    def creat_time = column[Long]("creat_time", NotNull)
    def creat_persn = column[String]("creat_persn", NotNull)
    def template = column[String]("template", NotNull)
    def api_visit_methd = column[Int]("api_visit_methd", NotNull)
    def plugin_type = column[Int]("plugin_type", NotNull)

    def pk = primaryKey("pk_plugin_info_id", (plugin_id))
    def * = (source_type_code, plugin_id, plugin_name, plugin_desc, class_name,creat_time, creat_persn, template, api_visit_methd, plugin_type) <> (PluginInfoRow.tupled, PluginInfoRow.unapply)

  }

  lazy val pluginInfo = TableQuery[PluginInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = pluginInfo.schema.create
    val sqlInfo = pluginInfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def drop() = {
    val sql = pluginInfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def loadPluginInfo: Seq[PluginInfoRow] = {
    val sql = pluginInfo.result
    val result = exec(sql)
    val pluginInfoMap = scala.collection.mutable.Map[Int, PluginInfoRow]()
    result
  }


}

object PluginInfoDao{

  def main(args: Array[String]) {
    val dao = new PluginInfoDao

    dao.create()

  }
}