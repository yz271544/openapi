package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiTabInfoRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class ApiTabInfoDao extends OpenApiLogging {

  val schemaName = "opi"
  val tableName = "api_tab_info"

  class ApiTabInfoTable(tag: Tag) extends Table[ApiTabInfoRow](tag, Option(schemaName), tableName) {

    def api_id = column[Int]("api_id", NotNull)

    def api_version = column[Int]("api_version", NotNull)

    def source_id = column[Int]("source_id", NotNull)

    def schema_name = column[String]("schema_name", NotNull)

    def tab_name = column[String]("tab_name", NotNull)

    def tab_alias = column[String]("tab_alias", NotNull)

    def field_name = column[String]("field_name", NotNull)

    def field_alias = column[String]("field_alias", NotNull)

    def field_type = column[String]("field_type", NotNull)

    def field_len = column[Int]("field_len", O.Default(0))

    def field_tot_digit = column[Int]("field_tot_digit", O.Default(0))

    def field_prec_digit = column[Int]("field_prec_digit", O.Default(0))


    def pk = primaryKey("pk_tab_info", (api_id, api_version, source_id, schema_name,
      tab_name, field_name, field_alias))

    def idx = index("idx_tab_info", (api_id, api_version, source_id, schema_name,
      tab_name, field_name, field_alias), unique = true)

    def * = (api_id, api_version, source_id, schema_name, tab_name, tab_alias, field_name, field_alias, field_type, field_len, field_tot_digit, field_prec_digit) <>(ApiTabInfoRow.tupled, ApiTabInfoRow.unapply)

  }

  lazy val apitabinfo: TableQuery[ApiTabInfoTable] = TableQuery[ApiTabInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = apitabinfo.schema.create
    val sqlInfo = apitabinfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = apitabinfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  /**
    * Key : ApiId,ApiVersion,SourceId
    * Value: SchemaName,TableName
    * @return
    */
  def load: mutable.Map[(Int, Int, Int), (String, String)] = {
    val sql = apitabinfo
      .groupBy{case (columns) => (columns.api_id,columns.api_version,columns.source_id,columns.schema_name,columns.tab_name)}
      .map{case (column) => column._1 }
      .result
    val resMap = scala.collection.mutable.Map[(Int, Int, Int), (String, String)]()
    val result = exec(sql)
    for (elem <- result) {
      resMap += ((elem._1, elem._2, elem._3) ->(elem._4, elem._5))
    }
    resMap
  }

  def loadByApiIdApiVersion(apiId: Int, apiVersion: Int) = {
    val sql = apitabinfo
      .groupBy{case (columns) => (columns.api_id,columns.api_version,columns.source_id,columns.schema_name,columns.tab_name)}
      .map{case (column) => column._1 }
      .filter{case (column) => column._1 === apiId && column._2 === apiVersion}
      .result
    val resMap = scala.collection.mutable.Map[(Int, Int, Int), (String, String)]()
    val result = exec(sql)
    for (elem <- result) {
      resMap += ((elem._1, elem._2, elem._3) ->(elem._4, elem._5))
    }
    resMap
  }

  def loadtest = {
    val sql = apitabinfo
        .groupBy{case (columns) => (columns.api_id,columns.api_version,columns.source_id,columns.schema_name,columns.tab_name)}
        .map{case (column) => column._1 }
      .result.statements
     sql
  }


  def loadByApiIdApiVersionTest(apiId: Int, apiVersion: Int) = {
    val sql = apitabinfo
      .groupBy{case (columns) => (columns.api_id,columns.api_version,columns.source_id,columns.schema_name,columns.tab_name)}
      .map{case (column) => column._1 }
      .filter{case (column) => column._1 === apiId && column._2 === apiVersion}
      //.groupBy(grp => (grp.api_id,grp.api_version,grp.source_id,grp.schema_name,grp.tab_name))
      //.groupBy{case (columns) => (columns.api_id,columns.api_version,columns.source_id,columns.schema_name,columns.tab_name)}

      //.map(m => (m.api_id,m.api_version,m.source_id,m.schema_name,m.tab_name))
      //.groupBy(grp => (grp._1,grp._2,grp._3,grp._4,grp._5))
      //.filter{column => column._1 === apiId && column._2 === apiVersion}
      //.map{case (column) => column._1 }
      .result.statements
    sql
  }

}

object ApiTabInfoDao {
  def apply() = {
    new ApiTabInfoDao()
  }
}

object apitabinfoDaoTest {
  def main(args: Array[String]) {
    val m = ApiTabInfoDao()
    println(m.loadtest)
    println("---------------------------------------------------")
    println(m.loadByApiIdApiVersionTest(1001,1))
  }
}
