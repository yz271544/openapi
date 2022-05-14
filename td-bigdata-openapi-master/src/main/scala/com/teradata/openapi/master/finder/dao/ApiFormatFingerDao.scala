package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiFormatFingerRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class ApiFormatFingerDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "api_form_finger"

  class ApiFormatFingerTable(tag: Tag) extends Table[ApiFormatFingerRow](tag, Option(schemaName), tableName) {

    def data_finger = column[String]("data_finger")

    def form_finger = column[String]("form_finger", NotNull)

    def file_loc = column[String]("file_loc")

    def eff_flag = column[Int]("eff_flag")

    def eff_time = column[Long]("eff_time")

    def exp_time = column[Long]("exp_time")

    def hit_times = column[Int]("hit_times", O.Default(0))

    def last_visit_time = column[Long]("last_visit_time", O.Default(System.currentTimeMillis()))

    def pk = primaryKey("pk_form_finger", (data_finger, form_finger))

    def idx_finger = index("idx_finger", form_finger, unique = true)

    def idx_data_finger = index("idx_form_finger", (data_finger, form_finger), unique = true)

    def * = (data_finger, form_finger, file_loc, hit_times, eff_flag, eff_time, exp_time, last_visit_time.?) <>(ApiFormatFingerRow.tupled, ApiFormatFingerRow.unapply)

  }

  lazy val apiformfinger: TableQuery[ApiFormatFingerTable] = TableQuery[ApiFormatFingerTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = apiformfinger.schema.create
    val sqlInfo = apiformfinger.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = apiformfinger.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  /**
    * 读取带格式指纹信息
    *
    * @return mutable.Map[form_finger:String -> (file_loc:String, data_finger:String, hit_times:Int, last_visit_time:Long)]
    */
  def loadFormFingerMap: scala.collection.mutable.Map[String, (String, String, Int, Long)] = {
    var formFingerMap = scala.collection.mutable.Map[String, (String, String, Int, Long)]()
    val sql = apiformfinger.filter(_.eff_flag === 1).map(m => (m.form_finger, m.file_loc, m.data_finger, m.hit_times, m.last_visit_time)).result
    val result = exec(sql)
    for (elem <- result) {
      formFingerMap += (elem._1 ->(elem._2, elem._3, elem._4, elem._5))
    }
    formFingerMap
  }

  /**
    * upsert for PostgreSQL
    * cause:  http://stackoverflow.com/questions/24579709/scalas-slick-with-multiple-pk-insertorupdate-throws-exception-error-syntax-e
    *
    * @return
    */
  def upsertFormFinger(form_finger: String, eff_flag: Int, file_loc: String = "", data_finger: String = "", hit_times: Option[Int] = None, last_visit_time: Option[Long] = None) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val currTime = System.currentTimeMillis()
    val insert = s"INSERT INTO $ModulesAffectedTableName (data_finger ,form_finger ,file_loc ,hit_times ,eff_flag ,eff_time ,exp_time,last_visit_time) SELECT '$data_finger','$form_finger','$file_loc',${hit_times.getOrElse(0)},$eff_flag,$currTime,$currTime,${last_visit_time.getOrElse(System.currentTimeMillis())}"
    val upsert = s"UPDATE $ModulesAffectedTableName SET eff_flag = $eff_flag, data_finger = '$data_finger',file_loc = '$file_loc' ,exp_time=$currTime,hit_times = ${hit_times.getOrElse("hit_times+1")},last_visit_time = ${last_visit_time.getOrElse(System.currentTimeMillis())} WHERE form_finger = '$form_finger'"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    log.debug("upsertFormFinger SQL:" + finalStmnt)
    exec(sqlu"#$finalStmnt")
  }

  def delFormFinger(dataFinger: String, formFinger: String): Int = {
    exec(apiformfinger.filter(_.data_finger === dataFinger).withFilter(_.form_finger === formFinger).delete)
  }

}

object ApiFormatFingerDao {
  def apply() = {
    new ApiFormatFingerDao()
  }
}

object ApiFormatFingerDaoTest {
  def main(args: Array[String]) {
    val m = ApiFormatFingerDao()
    //m.upsertFormFinger("formatFinger",1, Option("/adatakd/jka/hdfka/"),Option("dataFinger"))
    val formMap: mutable.Map[String, (String, String, Int, Long)] = m.loadFormFingerMap
    println("FROMMAP:" + formMap)
  }
}
