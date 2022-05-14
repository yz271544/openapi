package com.teradata.openapi.master.finder.dao

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{ApiDataSnpstDdInfoRow, DataSnpstVal, ListDataSnpst}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class ApiDataSnpstDdInfoDao extends OpenApiLogging {

  val opiSchemaName = "opi"
  val opiTableName = "api_data_snpst_dd_info"
  class ApiDataSnpstDdInfoTable(tag: Tag) extends Table[ApiDataSnpstDdInfoRow](tag, Option(opiSchemaName), opiTableName) {

    def source_id = column[Int]("source_id", NotNull)

    def schema_name = column[String]("schema_name", NotNull)

    def tab_name = column[String]("tab_name", NotNull)
/*

    def data_snpst_val = column[String]("data_snpst_val", O.Default(
    def idx = index("idx_snapshot", (source_id,schema_name,tab_name), unique = true)

    def * = (source_id,schema_name,tab_name,data_snpst_val) <> ("""{"dataSnpstVal":[]}"""))

    def pk = primaryKey("pk_snapshot", (source_id,schema_name,tab_name))
    ApiDataSnpstDdInfoRow.tupled, ApiDataSnpstDdInfoRow.unapply)
*/

    def data_snpst_val = column[String]("data_snpst_val", O.Default("""{"dataSnpstVal":[]}"""))

    def pk = primaryKey("pk_snapshot", (source_id,schema_name,tab_name))

    def idx = index("idx_snapshot", (source_id,schema_name,tab_name), unique = true)

    def * = (source_id,schema_name,tab_name,data_snpst_val) <> (ApiDataSnpstDdInfoRow.tupled, ApiDataSnpstDdInfoRow.unapply)


  }

  lazy val apidatasnpstddinfo: TableQuery[ApiDataSnpstDdInfoTable] = TableQuery[ApiDataSnpstDdInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def creatSnpst() = {
    val sql = apidatasnpstddinfo.schema.create
    val sqlInfo = apidatasnpstddinfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def dropSnpst() = {
    val sql = apidatasnpstddinfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e:Exception => throw e
    }
  }

  def loadSnpst: mutable.Map[(Int, String, String),DataSnpstVal] = {
    val sql = apidatasnpstddinfo.result
    val result = exec(sql)
    val dataSnpstMap = scala.collection.mutable.Map[(Int,String,String), DataSnpstVal]()
    for (elem <- result) {
      dataSnpstMap += ((elem.source_id,elem.schema_name,elem.tab_name) -> Json.parse[DataSnpstVal](elem.data_snpst_val))
    }
    dataSnpstMap
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    * @param sourceId
    * @param dataSnpstVal
    * @return
    */
  def upsertSnpst(sourceId: Int, schemaName :String, tableName :String, dataSnpstVal: DataSnpstVal = DataSnpstVal(List[ListDataSnpst]())) = {
    val dataSnpstValStr = Json.generate(dataSnpstVal)
    log.debug("upsertSnpst sourceId:" + sourceId + " schemaName:" + schemaName + " tableName:" + tableName + " dataSnpstValStr:" + dataSnpstValStr)
    println("upsertSnpst sourceId:" + sourceId + " schemaName:" + schemaName + " tableName:" + tableName + " dataSnpstValStr:" + dataSnpstValStr)
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$opiSchemaName.$opiTableName"
    val insert     = s"INSERT INTO $ModulesAffectedTableName (source_id,schema_name,tab_name, data_snpst_val) SELECT $sourceId,upper('$schemaName'),upper('$tableName'),'$dataSnpstValStr'"
    val upsert     = s"UPDATE $ModulesAffectedTableName SET data_snpst_val='$dataSnpstValStr' WHERE source_id=$sourceId AND schema_name = upper('$schemaName') AND tab_name=upper('$tableName')"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    log.debug("upsertSnpstSQL:" + finalStmnt)
    exec(sqlu"#$finalStmnt")
  }

  def upsertSnpstStatment(sourceId: Int, schemaName :String, tableName :String, dataSnpstVal: DataSnpstVal = DataSnpstVal(List[ListDataSnpst]())): String = {
    val dataSnpstValStr = Json.generate(dataSnpstVal)
    log.debug("upsertSnpst sourceId:" + sourceId + " schemaName" + schemaName + " tableName:" + tableName + " dataSnpstValStr:" + dataSnpstValStr)
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$opiSchemaName.$opiTableName"
    val insert     = s"INSERT INTO $ModulesAffectedTableName (source_id,schema_name,tab_name, data_snpst_val) SELECT $sourceId,'$schemaName','$tableName','$dataSnpstValStr'"
    val upsert     = s"UPDATE $ModulesAffectedTableName SET data_snpst_val='$dataSnpstValStr' WHERE source_id=$sourceId AND schema_name = '$schemaName' AND tab_name='$tableName'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    finalStmnt
  }


  def updateHitTimes(apiId:Int,apiVersion :Int, sourceId: Int, hitTimes: Option[Int] = None ,last_visit_time : Option[Long] = None) = {
    val ModulesAffectedTableName = s"$opiSchemaName.$opiTableName"
    val sql = s"UPDATE $ModulesAffectedTableName SET hit_times = ${hitTimes.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE api_id='$apiId' AND api_version = '$apiVersion' AND source_id='$sourceId'"
    exec(sqlu"#$sql")
  }


  def test(apiId:Int,apiVersion :Int, sourceId: Int, hitTimes: Option[Int] = None ,last_visit_time : Option[Long] = None) = {
    val ModulesAffectedTableName = s"$opiSchemaName.$opiTableName"
    val sql = s"UPDATE $ModulesAffectedTableName SET hit_times = ${hitTimes.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE api_id='$apiId' AND api_version = '$apiVersion' AND source_id='$sourceId'"
    sql
  }

  def delSnpst(sourceId: Int, schemaName :String, tableName :String): Int = {
    exec(apidatasnpstddinfo.filter(_.source_id === sourceId).withFilter(_.schema_name === schemaName).withFilter(_.tab_name === tableName).delete)
  }

}

object ApiDataSnpstDdInfoDao {
  def apply() = {
    new ApiDataSnpstDdInfoDao()
  }
}

object ApiDataSnpstDdInfoDaoTest {
  def main(args: Array[String]) {
    val m = ApiDataSnpstDdInfoDao()
    //m.dropSnpst()
    //m.creatSnpst()
    //m.writeSnpst("testAPIzyx",2,"{}",Some(100))
   //m.writeSnpst3("testAPIzyx",2,"{}",Some(100))
    //m.upsertSnpst(1234,1,1,"{}")
    println("SQL test:"+ m.test(1234,1,2,None,Some(System.currentTimeMillis())))
  }
}
