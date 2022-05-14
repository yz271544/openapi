package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiDataSnpstDdRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class ApiDataSnpstDdDao extends OpenApiLogging {

  val schemaName = "opi"
  val tableName = "api_data_snpst_dd"
  class ApiDataSnpstDdTable(tag: Tag) extends Table[ApiDataSnpstDdRow](tag, Option(schemaName), tableName) {

    def api_id = column[Int]("api_id" , NotNull)

    def api_version = column[Int]("api_version" , NotNull)

    def source_id = column[Int]("source_id", NotNull)

    def data_snpst_val = column[String]("data_snpst_val", O.Default("{}"))

    def hit_times = column[Int]("hit_times", O.Default(0))

    def last_visit_time = column[Long]("last_visit_time" , O.Default(System.currentTimeMillis()))

    def pk = primaryKey("pk_snapshot", (api_id,source_id))

    def idx = index("idx_snapshot", (api_id, api_version,source_id), unique = true)

    def * = (api_id, api_version, source_id, data_snpst_val, hit_times.?,last_visit_time.?) <> (ApiDataSnpstDdRow.tupled, ApiDataSnpstDdRow.unapply)

  }

  lazy val apidatasnpstdd: TableQuery[ApiDataSnpstDdTable] = TableQuery[ApiDataSnpstDdTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def creatSnpst() = {
    val sql = apidatasnpstdd.schema.create
    val sqlInfo = apidatasnpstdd.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def dropSnpst() = {
    val sql = apidatasnpstdd.schema.drop
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def loadSnpst: scala.collection.mutable.Map[String, String] = {
    val sql = apidatasnpstdd.result
    val result = exec(sql)
    val dataSnpstMap = scala.collection.mutable.Map[String, String]()
    result.foreach { e => dataSnpstMap += (e.api_id + "_" + e.api_version + "_" + e.source_id.toString -> e.data_snpst_val) }
    dataSnpstMap
  }

  def hasSnpstbyKey(apiId:Int,sourceId:Int): Boolean ={
    val containKey = apidatasnpstdd.filter(m => m.api_id === apiId && m.source_id === sourceId).exists.result
    exec(containKey)
  }

  def hasSnpstbyKey2(apiId:Int,sourceId:Int): Option[Int] ={
    val containKey = apidatasnpstdd.filter(m => m.api_id === apiId && m.source_id === sourceId).map(_.hit_times).sum.result
    exec(containKey)
  }

  /**
    * For MySQL and Oracle
    * @param apiId
    * @param sourceId
    * @param dataSnpstVal
    * @param hitTimes
    * @return
    */
  def writeSnpst(apiId: Int, apiVersion :Int, sourceId: Int, dataSnpstVal: String, hitTimes: Option[Int] = None , last_visit_time : Option[Long] = None): Int = {
    try {
      exec(apidatasnpstdd.insertOrUpdate(ApiDataSnpstDdRow(apiId,apiVersion, sourceId, dataSnpstVal, hitTimes,last_visit_time)))
    }
    catch {
      case  e :Exception => throw e
    }
    finally {}
  }

  def writeSnpst2(apiId :Int,apiVersion :Int, sourceId: Int, dataSnpstVal: String, hitTimes: Option[Int] = None, last_visit_time : Option[Long] = None) = {
    var updRowNum :Int = 0
    val row = ApiDataSnpstDdRow(apiId,apiVersion,sourceId,dataSnpstVal,Some(hitTimes.getOrElse(0)), Some(last_visit_time.getOrElse(System.currentTimeMillis())))
    val hasKey = hasSnpstbyKey(apiId,sourceId)
    if (hasKey){
      println("Update Statements :::::::::::::::::::::::" + apidatasnpstdd.update(row).statements)
      updRowNum = exec(apidatasnpstdd.update(row))
    } else {
      println("Insert:" + row)
      updRowNum = exec(apidatasnpstdd.forceInsert(row))
    }
  }

  def writeSnpst3(apiId:Int, apiVersion :Int, sourceId: Int, dataSnpstVal: String, hitTimes: Option[Int] = None, last_visit_time : Option[Long] = None) = {
    var updRowNum :Int = 0
    var row = ApiDataSnpstDdRow(apiId,apiVersion,sourceId,dataSnpstVal,Some(hitTimes.getOrElse(0)),Some(last_visit_time.getOrElse(System.currentTimeMillis())))
    val hasKey = hasSnpstbyKey2(apiId,sourceId)
    if (hasKey.isEmpty){
      println("Insert:" + row)
      updRowNum = exec(apidatasnpstdd.forceInsert(row))
    } else {
      row = ApiDataSnpstDdRow(apiId,apiVersion,sourceId,dataSnpstVal,Some(hasKey.getOrElse(0)),Some(last_visit_time.getOrElse(System.currentTimeMillis())))
      println("Update Statements :::::::::::::::::::::::" + apidatasnpstdd.update(row).statements)
      updRowNum = exec(apidatasnpstdd.filter(_.api_id === apiId).filter(_.source_id === sourceId).update(row))
    }
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    * @param apiId
    * @param sourceId
    * @param dataSnpstVal
    * @param hitTimes
    * @return
    */
  def upsertSnpst(apiId:Int,apiVersion :Int, sourceId: Int, dataSnpstVal: String, hitTimes: Option[Int] = None ,last_visit_time : Option[Long] = None) = {
    log.debug("upsertSnpst:" + apiId + " apiVersion" + apiVersion + " sourceId:" + sourceId + " dataSnpstVal:" + dataSnpstVal)
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val insert     = s"INSERT INTO $ModulesAffectedTableName (api_id, api_version, source_id, data_snpst_val,hit_times,last_visit_time) SELECT $apiId,$apiVersion,'$sourceId','$dataSnpstVal','${hitTimes.getOrElse(0)}',${last_visit_time.getOrElse(System.currentTimeMillis())}"
    val upsert     = s"UPDATE $ModulesAffectedTableName SET data_snpst_val='$dataSnpstVal',hit_times = ${hitTimes.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE api_id='$apiId' AND api_version = '$apiVersion' AND source_id='$sourceId'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }

  def updateHitTimes(apiId:Int,apiVersion :Int, sourceId: Int, hitTimes: Option[Int] = None ,last_visit_time : Option[Long] = None) = {
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val sql = s"UPDATE $ModulesAffectedTableName SET hit_times = ${hitTimes.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE api_id='$apiId' AND api_version = '$apiVersion' AND source_id='$sourceId'"
    exec(sqlu"#$sql")
  }


  def test(apiId:Int,apiVersion :Int, sourceId: Int, hitTimes: Option[Int] = None ,last_visit_time : Option[Long] = None) = {
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val sql = s"UPDATE $ModulesAffectedTableName SET hit_times = ${hitTimes.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE api_id='$apiId' AND api_version = '$apiVersion' AND source_id='$sourceId'"
    sql
  }

  def delSnpst(apiId: Int,apiVersion:Int, sourceId: Int): Int = {
    exec(apidatasnpstdd.filter(_.api_id === apiId).withFilter(_.api_version === apiVersion).withFilter(_.source_id === sourceId).delete)
  }

}

object ApiDataSnpstDdDao {
  def apply() = {
    new ApiDataSnpstDdDao()
  }
}

object ApiDataSnpstDdDaoTest {
  def main(args: Array[String]) {
    val m = ApiDataSnpstDdDao()
    //m.dropSnpst()
    //m.creatSnpst()
    //m.writeSnpst("testAPIzyx",2,"{}",Some(100))
   //m.writeSnpst3("testAPIzyx",2,"{}",Some(100))
    //m.upsertSnpst(1234,1,1,"{}")
    println("SQL test:"+ m.test(1234,1,2,None,Some(System.currentTimeMillis())))
  }
}
