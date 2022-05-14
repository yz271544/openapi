package com.teradata.openapi.master.cacher.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiDataSnpstCacheInfoRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/6/29.
  */
class ApiDataSnpstCacheInfoDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_data_snpst_cache_info"

  class ApiDataSnpstCacheInfoTable(tag: Tag) extends Table[ApiDataSnpstCacheInfoRow](tag, Option(schemaName), tableName) {

    def schema_name = column[String]("schema_name", NotNull)

    def tab_name = column[String]("tab_name", NotNull)

    def data_snpst_name_val = column[String]("data_snpst_name_val", O.Default("{}"))

    def isnt_cache = column[Int]("isnt_cache", O.Default(0))

    def hit_times = column[Int]("hit_times", O.Default(0))

    def last_visit_time = column[Long]("last_visit_time" , O.Default(System.currentTimeMillis()))

    def pk = primaryKey("pk_snapshot_cache", (schema_name, tab_name))

    def * = (schema_name, tab_name, data_snpst_name_val, isnt_cache, hit_times.?, last_visit_time.?) <>(ApiDataSnpstCacheInfoRow.tupled, ApiDataSnpstCacheInfoRow.unapply)

  }

  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  lazy val apidatasnpstcacheinfodd: TableQuery[ApiDataSnpstCacheInfoTable] = TableQuery[ApiDataSnpstCacheInfoTable]

  def creatSnpstCacheInfo() = {
    val sql = apidatasnpstcacheinfodd.schema.create
    val sqlInfo = apidatasnpstcacheinfodd.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def dropSnpstCacheInfo() = {
    val sql = apidatasnpstcacheinfodd.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadAllCacheInfo(): Seq[ApiDataSnpstCacheInfoRow] = {
    val addCachListQry = apidatasnpstcacheinfodd.filter(_.isnt_cache =!= 3).result
    val result = exec(addCachListQry)
    result
  }

  def test() = {
    val addCachListQry = apidatasnpstcacheinfodd.filter(_.isnt_cache =!= 3).result.statements
    //val result = exec(addCachListQry)
    //result
    addCachListQry
  }

  def insert(schema_name: String, tab_name: String, data_snpst_name_val :String, isnt_cache:Int, hit_times:Option[Int] = None, last_visit_time:Option[Long] = None) = {
    val action = apidatasnpstcacheinfodd += ApiDataSnpstCacheInfoRow(schema_name, tab_name, data_snpst_name_val, isnt_cache, hit_times, last_visit_time)
    exec(action)
  }

  /**
    * 主要用于增加缓存切片，并更新缓存命中次数，最后一次访问时间
    * @param schema_name
    * @param tab_name
    * @param data_snpst_name_val
    * @param isnt_cache
    * @param hit_times
    * @param last_visit_time
    * @return
    */
  def upsert(schema_name: String, tab_name: String, data_snpst_name_val :String, isnt_cache:Int, hit_times:Option[Int] = None, last_visit_time:Option[Long] = None) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val insert     = s"INSERT INTO $ModulesAffectedTableName (schema_name, tab_name, data_snpst_name_val, isnt_cache,hit_times, last_visit_time) SELECT '$schema_name','$tab_name','$data_snpst_name_val','$isnt_cache','${hit_times.getOrElse(0)}',${last_visit_time.getOrElse(System.currentTimeMillis())}"
    val upsert     = s"UPDATE $ModulesAffectedTableName SET isnt_cache=$isnt_cache, hit_times = ${hit_times.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE schema_name='$schema_name' AND tab_name = '$tab_name' AND data_snpst_name_val='$data_snpst_name_val'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }


  def updHitTimes(schema_name: String, tab_name: String, data_snpst_name_val :String, isnt_cache:Int, hit_times:Option[Int] = None, last_visit_time:Option[Long] = None) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val updHitTimes = s"UPDATE $ModulesAffectedTableName SET hit_times = ${hit_times.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE schema_name='$schema_name' AND tab_name = '$tab_name' AND data_snpst_name_val='$data_snpst_name_val' AND isnt_cache=$isnt_cache"
    exec(sqlu"#$updHitTimes")
  }
  /**
    * 修改匹配到的缓存表的快照切片的状态
    * @param schema_name
    * @param tab_name
    * @param data_snpst_name_val
    * @param isnt_cache
    */
  def updateMatchCacheStat(schema_name: String, tab_name: String , data_snpst_name_val :String , isnt_cache:Int) = {
    val updAction = apidatasnpstcacheinfodd.filter(_.schema_name === schema_name).withFilter(_.tab_name === tab_name).withFilter(_.data_snpst_name_val === data_snpst_name_val)
      .map(m => m.isnt_cache ).update(isnt_cache)
    val retRows = exec(updAction)
  }

  /**
    * 修改匹配到的缓存表的所有切片状态
    * @param schema_name
    * @param tab_name
    * @param isnt_cache
    */
  def updateAllMatchCacheStat(schema_name: String, tab_name: String , isnt_cache:Int) = {
    val updAction = apidatasnpstcacheinfodd.filter(_.schema_name === schema_name).withFilter(_.tab_name === tab_name)
      .map(m => m.isnt_cache ).update(isnt_cache)
    val retRows = exec(updAction)
  }

}


object ApiDataSnpstCacheInfoDao {
  def apply() = {
    new ApiDataSnpstCacheInfoDao()
  }
}

object ApiDataSnpstCacheInfoDaoTest {
  def main(args: Array[String]) {
    println("test:")
    val m = ApiDataSnpstCacheInfoDao()
    //m.dropSnpst()
    //m.creatSnpstCacheInfo()
    //m.writeSnpst("testAPIzyx",2,"{}",Some(100))
    //m.writeSnpst3("testAPIzyx",2,"{}",Some(100))
    //m.upsertSnpstCacheInfo(1234,1,1,"{}")
    /*val aa = m.loadAllCacheInfo()
    for (elem <- aa) {
      println("data:" + elem)
    }*/
  }
}