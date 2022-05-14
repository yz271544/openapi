package com.teradata.openapi.master.deploy.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{ApiDataSnpstCacheInfoRow, ApiSysArgRow}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/7/15.
  */
class ApiSysArgDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "sys_arg"

  class ApiSysArgTable(tag: Tag) extends Table[ApiSysArgRow](tag, Option(schemaName), tableName) {

    def sys_arg_name = column[String]("sys_arg_name", NotNull)

    def sys_arg_val = column[String]("sys_arg_val", NotNull)

    def pk = primaryKey("pk_snapshot_cache", sys_arg_name)

    def * = (sys_arg_name, sys_arg_val) <>(ApiSysArgRow.tupled, ApiSysArgRow.unapply)

  }

  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  lazy val apisysarg: TableQuery[ApiSysArgTable] = TableQuery[ApiSysArgTable]

  def create() = {
    val sql = apisysarg.schema.create
    val sqlInfo = apisysarg.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = apisysarg.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def load(): Seq[ApiSysArgRow] = {
    val addCachListQry = apisysarg.result
    val result = exec(addCachListQry)
    result
  }

  def insert(sys_arg_name: String, sys_arg_val: String) = {
    val action = apisysarg += ApiSysArgRow(sys_arg_name, sys_arg_val)
    exec(action)
  }

  /**
    * 修改或插入系统变量
    * @param sys_arg_name 系统变量名称
    * @param sys_arg_val 系统变量值
    * @return
    */
  def upsert(sys_arg_name: String, sys_arg_val: String) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val insert = s"INSERT INTO $ModulesAffectedTableName (sys_arg_name, sys_arg_val) SELECT '$sys_arg_name','$sys_arg_val'"
    val upsert = s"UPDATE $ModulesAffectedTableName SET sys_arg_val='$sys_arg_val' WHERE sys_arg_name='$sys_arg_name'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }
}


object ApiSysArgDao {
  def apply() = {
    new ApiSysArgDao()
  }
}

object ApiSysArgDaoTest {
  def main(args: Array[String]) {
    println("test:")
    val m = ApiSysArgDao()
  }
}