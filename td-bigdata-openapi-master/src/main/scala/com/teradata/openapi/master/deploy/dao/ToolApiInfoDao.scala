package com.teradata.openapi.master.deploy.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ToolApiInfoRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class ToolApiInfoDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "tool_api_info"

  class ToolApiInfoTable(tag: Tag) extends Table[ToolApiInfoRow](tag, Option(schemaName), tableName) {

    def api_id = column[Int]("api_id", NotNull)

    def api_version = column[Int]("api_version", NotNull)

    def api_name = column[String]("api_name", NotNull)

    def tool_api_id = column[String]("tool_api_id", NotNull)

    def api_msg_name = column[String]("api_msg_name", NotNull)

    def api_class_name = column[String]("api_class_name", NotNull)

    def creat_time = column[Long]("creat_time", NotNull)

    def * = (api_id, api_version, api_name, tool_api_id, api_msg_name, api_class_name, creat_time) <>(ToolApiInfoRow.tupled, ToolApiInfoRow.unapply)

  }

  lazy val toolapiinfo: TableQuery[ToolApiInfoTable] = TableQuery[ToolApiInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = toolapiinfo.schema.create
    val sqlInfo = toolapiinfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = toolapiinfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadAll: Seq[ToolApiInfoRow] = {
    val sql = toolapiinfo.result
    val result = exec(sql)
    result
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    *
    * @return
    */
  def upsert(api_id: Int, api_version: Int, api_name: String, tool_api_id: String, api_msg_name: String, api_class_name: String, creat_time: Option[Long] = None) = {
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val currTime = System.currentTimeMillis()
    val insert = s" INSERT INTO $ModulesAffectedTableName (api_id, api_version, api_name, tool_api_id, api_msg_name, api_class_name, creat_time) SELECT $api_id,$api_version,'$api_name','$tool_api_id','$api_msg_name','$api_class_name',${creat_time.getOrElse(System.currentTimeMillis())} "
    val upsert = s" UPDATE $ModulesAffectedTableName SET api_name='$api_name',tool_api_id = '$tool_api_id',api_msg_name = '$api_msg_name',api_class_name='$api_class_name',creat_time = ${creat_time.getOrElse(System.currentTimeMillis())} WHERE api_id = $api_id and api_version = $api_version "
    val finalStmnt = s" WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert) "
    exec(sqlu"#$finalStmnt")
  }

  def delDataFinger(apiId: Int, apiVersion: Int): Int = {
    exec(toolapiinfo.filter(_.api_id === apiId).withFilter(_.api_version === apiVersion).delete)
  }

}

object ToolApiInfoDao {
  def apply() = {
    new ToolApiInfoDao()
  }
}

object ToolApiInfoTest {
  def main(args: Array[String]) {
    val m = ToolApiInfoDao()
    //m.drop()
    //m.create()
    //m.upsertDataFinger(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
  }
}
