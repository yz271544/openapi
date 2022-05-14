package com.teradata.openapi.master.finder.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{FinderCacheFingerRow}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.jdbc.GetResult
import slick.lifted.ProvenShape
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/5/19.
  */
class FinderCacheFingerDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "finder_cyclercd_formfinger_rel"

  class FinderCacheFingerTable(tag: Tag) extends Table[FinderCacheFingerRow](tag, Option(schemaName), tableName) {
    def req_id = column[String]("req_id", NotNull)

    def api_id = column[Int]("api_id")

    def api_version = column[Int]("api_version")

    def schema_name = column[String]("schema_name")

    def tab_name = column[String]("tab_name")

    def cyclecolumn = column[String]("cyclecolumn")

    def cyclevalues = column[String]("cyclevalues")

    def form_finger = column[String]("form_finger")

    def * = (req_id, api_id, api_version, schema_name, tab_name, cyclecolumn, cyclevalues, form_finger) <>(FinderCacheFingerRow.tupled, FinderCacheFingerRow.unapply)
  }
  implicit val GetReqInfoVwRow = GetResult(r =>
    FinderCacheFingerRow(req_id = r.<<,
      api_id = r.<<,
      api_version = r.<<,
      schema_name = r.<<,
      tab_name = r.<<,
      cyclecolumn = r.<<,
      cyclevalues = r.<<,
      form_finger = r.<<))
  lazy val finderCacheFinger: TableQuery[FinderCacheFingerTable] = TableQuery[FinderCacheFingerTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)


  def loadALL: Seq[FinderCacheFingerRow] = {
    val sql = finderCacheFinger.result
    val result = exec(sql)
    result
  }

  def loadByCycleInfo(cycleSchemaName: String, tabName: String, cycleColumn: String, cycleValues: List[String]): Seq[FinderCacheFingerRow] = {
    val cycleSchemaNameFormat = cycleSchemaName.mkString("'","","'")
    val tabNameFormat = tabName.mkString("'","","'")
    val cycleColumnFormat = cycleColumn.mkString("'","","'")
    val cycleValuesFormat = cycleValues.mkString("'", "','", "'")
    val sql = sql"""
  select
  req_id, api_id ,api_version ,schema_name ,tab_name ,cyclecolumn ,cyclevalues ,form_finger
  from
  #$schemaName.#$tableName(#$cycleSchemaNameFormat,#$tabNameFormat,#$cycleColumnFormat)
  where
  cyclevalues in (#$cycleValuesFormat)""".as[FinderCacheFingerRow]
    //log.debug("loadByTriggerSorc SQL:" + sql.statements.toString)
    //println("loadByTriggerSorc SQL:" + sql.statements.toString)
    val result = exec(sql)
    result
  }

}

object FinderCacheFingerDao {
  def apply() = {
    new FinderCacheFingerDao()
  }
}

object FinderCacheFingerDaoTest {
  def main(args: Array[String]) {
    val m = FinderCacheFingerDao()
    //m.upsert(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
    val ret: Seq[FinderCacheFingerRow] = m.loadByCycleInfo("RPTMART3","TB_RPT_BO_MON","DEAL_DATE",List("201604","201605"))
    println(ret)
  }
}
