package com.teradata.openapi.master.cacher.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.ApiCacheRulesRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by John on 2016/7/7.
  */
class ApiCacheRulesDao extends OpenApiLogging {
  val schemaName = "opi"
  val tableName = "cache_rule"

  class ApiCacheRulesTable(tag: Tag) extends Table[ApiCacheRulesRow](tag, Option(schemaName), tableName) {
    def rule_id = column[Int]("rule_id", O.PrimaryKey)

    def rule_type = column[Int]("rule_type")

    //1 进缓存 0 清缓存
    def rule_name = column[String]("rule_name")

    def rule_range = column[String]("rule_range", O.Default("{}"))

    def rule_priority = column[Int]("rule_priority")

    def hit_times_min = column[Int]("hit_times_min")

    def visit_time_max = column[Int]("visit_time_max")

    def visit_time_unit = column[String]("visit_time_unit")

    def rule_chg_time = column[Long]("rule_chg_time", O.Default(System.currentTimeMillis()))

    def eff_stat = column[Int]("eff_stat")

    def * = (rule_id, rule_type, rule_name, rule_range, rule_priority, hit_times_min.?, visit_time_max.?, visit_time_unit.?, rule_chg_time, eff_stat) <>(ApiCacheRulesRow.tupled, ApiCacheRulesRow.unapply)

  }

  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  lazy val apicacherulesdd: TableQuery[ApiCacheRulesTable] = TableQuery[ApiCacheRulesTable]

  def creatCacheRules() = {
    val sql = apicacherulesdd.schema.create
    val sqlInfo = apicacherulesdd.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def dropCacheRules() = {
    val sql = apicacherulesdd.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadAllCacheRules(): Seq[ApiCacheRulesRow] = {
    val addCachRulesQry = apicacherulesdd.filter(_.eff_stat === 1).sortBy(_.rule_priority.desc.nullsLast).result
    val result = exec(addCachRulesQry)
    result
  }


  def insert(rule_id: Int, rule_type: Int, rule_name: String, rule_range: String, rule_priority: Int, hit_times_min: Option[Int] = None, visit_time_max: Option[Int] = None, visit_time_unit: Option[String] = None, rule_chg_time: Option[Long] = None , eff_stat: Int) = {
    val action = apicacherulesdd += ApiCacheRulesRow(rule_id, rule_type, rule_name, rule_range, rule_priority, hit_times_min, visit_time_max, visit_time_unit, rule_chg_time.getOrElse(System.currentTimeMillis()), eff_stat)
    exec(action)
  }


  def upsert(rule_id: Int, rule_type: Int, rule_name: String, rule_range: String, rule_priority: Int, hit_times_min: Int, visit_time_max: Int, visit_time_unit: String, rule_chg_time: Option[Long], eff_stat: Int) = {
    // see this article http://www.the-art-of-web.com/sql/upsert/
    val ModulesAffectedTableName = s"$schemaName.$tableName"
    val insert = s"INSERT INTO $ModulesAffectedTableName (rule_id, rule_type, rule_name, rule_range, rule_priority, hit_times_min,visit_time_max,visit_time_unit,rule_chg_time,eff_stat) " +
      s"SELECT $rule_id,$rule_type,'$rule_name','$rule_range',$rule_priority,$hit_times_min,$visit_time_max,'$visit_time_unit',$rule_chg_time,$eff_stat "
    val upsert = s"UPDATE $ModulesAffectedTableName SET rule_type=$rule_type, rule_name = '$rule_name',rule_range= '$rule_range',rule_priority = $rule_priority,hit_times_min=$hit_times_min,visit_time_max=$visit_time_max,visit_time_unit='$visit_time_unit',rule_chg_time = ${rule_chg_time.getOrElse(System.currentTimeMillis())},eff_stat = $eff_stat " +
      s"WHERE rule_id=$rule_id"
    val finalStmnt = s"WITH upsert AS ($upsert RETURNING *) $insert WHERE NOT EXISTS (SELECT * FROM upsert)"
    exec(sqlu"#$finalStmnt")
  }

}


object ApiCacheRulesDao {
  def apply() = {
    new ApiCacheRulesDao()
  }
}

object ApiCacheRulesDaoTest {
  def main(args: Array[String]) {
    val m = ApiCacheRulesDao()
  }
}