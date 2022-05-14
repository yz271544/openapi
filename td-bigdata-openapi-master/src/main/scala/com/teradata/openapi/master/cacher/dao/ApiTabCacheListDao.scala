package com.teradata.openapi.master.cacher.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiTabCacheListRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/6/29.
  */
class ApiTabCacheListDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_tab_cache_list"

  class ApiTabCacheListTable(tag: Tag) extends Table[ApiTabCacheListRow](tag, Option(schemaName), tableName) {

    def schema_name = column[String]("schema_name", NotNull)

    def tab_name = column[String]("tab_name", NotNull)

    def refresh_time = column[Long]("refresh_time")

    def hit_times = column[Int]("hit_times", O.Default(0))

    def pk = primaryKey("pk_snapshot_cache", (schema_name, tab_name))

    def * = (schema_name, tab_name, refresh_time,hit_times.?) <> (ApiTabCacheListRow.tupled, ApiTabCacheListRow.unapply)

  }
  lazy val apitabcachelistdd: TableQuery[ApiTabCacheListTable] = TableQuery[ApiTabCacheListTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def creat() = {
    val sql = apitabcachelistdd.schema.create
    val sqlInfo = apitabcachelistdd.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def drop() = {
    val sql = apitabcachelistdd.schema.drop
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def loadAllCacheList(): Seq[ApiTabCacheListRow] = {
    val addCachListQry = apitabcachelistdd.result
    val result = exec(addCachListQry)
    result
  }
}


object ApiTabCacheListDao {
  def apply() = {
    new ApiTabCacheListDao()
  }
}

object ApiTabCacheListDaoTest {
  def main(args: Array[String]) {
    val m = ApiTabCacheListDao()
    //m.dropSnpst()
    //m.creat()
    //m.writeSnpst("testAPIzyx",2,"{}",Some(100))
    //m.writeSnpst3("testAPIzyx",2,"{}",Some(100))
    //m.upsertSnpstCacheInfo(1234,1,1,"{}")

    val aa = m.loadAllCacheList()
    for (elem <- aa) {
      println("data:" + elem)
    }

  }
}