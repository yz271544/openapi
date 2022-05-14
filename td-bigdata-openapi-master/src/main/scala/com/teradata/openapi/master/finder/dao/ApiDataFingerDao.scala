package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiDataFingerRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class ApiDataFingerDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_data_finger"

  class ApiDataFingerTable(tag: Tag) extends Table[ApiDataFingerRow](tag, Option(schemaName), tableName) {

    def data_finger = column[String]("data_finger", NotNull)

    def file_loc = column[String]("file_loc", NotNull)

    def encode = column[String]("encode", NotNull)

    def eff_flag = column[Int]("eff_flag")

    def eff_time = column[Long]("eff_time")

    def exp_time = column[Long]("exp_time")

    def hit_times = column[Int]("hit_times", O.Default(0))

    def last_visit_time = column[Long]("last_visit_time", O.Default(System.currentTimeMillis()))

    def pk = primaryKey("pk_data_finger", data_finger)

    def idx_finger = index("idx_finger", data_finger, unique = true)

    def idx_data_finger = index("idx_data_finger", data_finger, unique = true)

    def * = (data_finger, file_loc, encode, hit_times, eff_flag, eff_time, exp_time, last_visit_time.?) <>(ApiDataFingerRow.tupled, ApiDataFingerRow.unapply)

  }

  lazy val apidatafinger: TableQuery[ApiDataFingerTable] = TableQuery[ApiDataFingerTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = apidatafinger.schema.create
    val sqlInfo = apidatafinger.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = apidatafinger.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  /**
    * 读取数据指纹信息
    *
    * @return mutable.Map[data_finger:String -> (file_loc:String, hit_times:Int, last_visit_time:Long)]
    */
  def loadDataFingerMap: mutable.Map[String, (String, String, Int, Long)] = {
    var dataFingerMap = scala.collection.mutable.Map[String, (String, String, Int, Long)]()
    val sql = apidatafinger.filter(_.eff_flag === 1).map(m => (m.data_finger, m.file_loc, m.encode, m.hit_times, m.last_visit_time)).result
    val result = exec(sql)
    for (elem <- result) {
      dataFingerMap += (elem._1 ->(elem._2, elem._3, elem._4, elem._5))
    }
    dataFingerMap
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    *
    * @return
    */
  def upsertDataFinger(data_finger: String, eff_flag: Int, file_loc: String = "", encode: String = "", hit_times: Option[Int] = None, last_visit_time: Option[Long] = None) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val currTime = System.currentTimeMillis()
    val insert = s"INSERT INTO $ModulesAffectedTableName (data_finger ,file_loc ,encode,hit_times ,eff_flag ,eff_time ,exp_time,last_visit_time) SELECT '$data_finger','$file_loc','$encode',${hit_times.getOrElse(0)},$eff_flag,$currTime,$currTime,${last_visit_time.getOrElse(System.currentTimeMillis())}"
    val upsert = s"UPDATE $ModulesAffectedTableName SET eff_flag = $eff_flag,file_loc = '$file_loc',encode = '$encode',exp_time=$currTime,hit_times = ${hit_times.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE data_finger = '$data_finger'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    log.debug("upsertDataFinger SQL:" + finalStmnt)
    exec(sqlu"#$finalStmnt")
  }

  def delDataFinger(dataFinger: String): Int = {
    exec(apidatafinger.filter(_.data_finger === dataFinger).delete)
  }

}

object ApiDataFingerDao {
  def apply() = {
    new ApiDataFingerDao()
  }
}

object ApiDataFingerDaoTest {
  def main(args: Array[String]) {
    val m = ApiDataFingerDao()
    //m.drop()
    //m.create()
    //m.upsertDataFinger(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
  }
}
