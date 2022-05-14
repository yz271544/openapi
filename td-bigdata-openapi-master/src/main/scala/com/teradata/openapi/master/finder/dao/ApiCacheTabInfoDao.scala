package com.teradata.openapi.master.finder.dao

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{ApiCacheTabInfoRow, ApiDataSnpstDdInfoRow, DataSnpst, DataSnpstVal}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class ApiCacheTabInfoDao extends OpenApiLogging {

  val opiSchemaName = "opi"
  val opiTableName = "api_cache_tab_info"

  class ApiCachTabInfoTable(tag: Tag) extends Table[ApiCacheTabInfoRow](tag, Option(opiSchemaName), opiTableName) {

    def api_id = column[Int]("api_id", NotNull)

    def api_version = column[Int]("api_version", NotNull)

    def source_id = column[Int]("source_id", NotNull)

    def schema_name = column[String]("schema_name", NotNull)

    def tab_name = column[String]("tab_name", NotNull)

    def pk = primaryKey("pk_cachetab", (api_id, api_version, source_id))

    def idx = index("idx_cachetab", (api_id, api_version, source_id), unique = true)

    def * = (api_id, api_version, source_id, schema_name, tab_name) <>(ApiCacheTabInfoRow.tupled, ApiCacheTabInfoRow.unapply)

  }

  lazy val apicachetabinfo: TableQuery[ApiCachTabInfoTable] = TableQuery[ApiCachTabInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def creatCache() = {
    val sql = apicachetabinfo.schema.create
    val sqlInfo = apicachetabinfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def dropCache() = {
    val sql = apicachetabinfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadCache: Seq[ApiCacheTabInfoRow] = {
    val sql = apicachetabinfo.result
    val result = exec(sql)
    result
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    *
    * @param sourceId
    * @return
    */
  def upsertCache(apiId: Int, apiVersion: Int, sourceId: Int, schemaName: String, tableName: String) = {
    println("upsertCache:apiId:" + apiId + " apiVersion:" + apiVersion + " sourceId:" + sourceId + " schemaName:" + schemaName + " tableName:" + tableName)
    log.debug("upsertCache:apiId:" + apiId + " apiVersion:" + apiVersion + " sourceId:" + sourceId + " schemaName:" + schemaName + " tableName:" + tableName)
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$opiSchemaName.$opiTableName"
    val insert = s"INSERT INTO $ModulesAffectedTableName (api_id,api_version, source_id,schema_name,tab_name) SELECT $apiId,$apiVersion,$sourceId,'$schemaName','$tableName'"
    val upsert = s"UPDATE $ModulesAffectedTableName SET schema_name = '$schemaName' , tab_name='$tableName' WHERE api_id=$apiId AND api_version = $apiVersion AND source_id=$sourceId"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    println("upsertCacheSQL:" + finalStmnt)
    log.debug("upsertCacheSQL:" + finalStmnt)
    exec(sqlu"#$finalStmnt")
  }

}

object ApiCacheTabInfoDao {
  def apply() = {
    new ApiCacheTabInfoDao()
  }
}

object ApiCacheTabInfoDaoTest {
  def main(args: Array[String]) {
    val m = ApiCacheTabInfoDao()
    //m.dropSnpst()
    //m.creatSnpst()
    //m.writeSnpst("testAPIzyx",2,"{}",Some(100))
    //m.writeSnpst3("testAPIzyx",2,"{}",Some(100))
    //m.upsertSnpst(1234,1,1,"{}")
    //println("SQL test:"+ m.test(1234,1,2,None,Some(System.currentTimeMillis())))
  }
}
