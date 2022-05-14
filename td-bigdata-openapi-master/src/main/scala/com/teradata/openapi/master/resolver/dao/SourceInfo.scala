package com.teradata.openapi.master.resolver.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.SourceInfoRow
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by lzf on 2016/5/12.
  */

class SourceInfoDao extends OpenApiLogging {

  class SourceInfoTable(tag: Tag) extends Table[SourceInfoRow](tag, Option("opi"), "source_info_vw") {

    def source_id = column[Int]("source_id", NotNull, O.AutoInc)

    def source_type_code = column[String]("source_type_code", NotNull)

    def drv_code = column[Int]("drv_code", NotNull)

    def source_desc = column[String]("source_desc", NotNull)

    def ip_addr = column[String]("ip_addr", NotNull)

    def port = column[Int]("port", NotNull)

    def deflt_schema = column[String]("deflt_schema", NotNull)

    def user_name = column[String]("user_name", NotNull)

    def pwd = column[String]("pwd", NotNull)

    def priority = column[Int]("priority", NotNull)

    def sync_strategy_id = column[Int]("sync_strategy_id", NotNull)

    def asyn_strategy_id = column[Int]("asyn_strategy_id", NotNull)

    def drv_name = column[String]("drv_name", NotNull)

    def sync_finder_flag = column[Boolean]("sync_finder_flag")

    def asyn_finder_flag = column[Boolean]("asyn_finder_flag")

    def rss_finder_flag = column[Boolean]("rss_finder_flag")

    def pk = primaryKey("pk_sourceId", (source_id))

    def * = (source_id, source_type_code, drv_code, source_desc, ip_addr, port, deflt_schema, user_name, pwd, priority, sync_strategy_id, asyn_strategy_id, drv_name, sync_finder_flag, asyn_finder_flag, rss_finder_flag) <>(SourceInfoRow.tupled, SourceInfoRow.unapply)

  }

  lazy val sourceInfo = TableQuery[SourceInfoTable]
  val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")

  def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

  def create() = {
    val sql = sourceInfo.schema.create
    val sqlInfo = sourceInfo.schema.createStatements
    println(sqlInfo)
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def drop() = {
    val sql = sourceInfo.schema.drop
    try {
      exec(sql)
    } catch {
      case e: Exception => throw e
    }
  }

  def loadSourceInfoMap: scala.collection.mutable.Map[Int, SourceInfoRow] = {
    val sql = sourceInfo.result
    val result = exec(sql)
    val sourceInfoMap = scala.collection.mutable.Map[Int, SourceInfoRow]()
    result.foreach { e => sourceInfoMap += (e.source_id -> e) }
    sourceInfoMap
  }

  def loadSourceIdList: List[Int] = {
    val sql = sourceInfo.sortBy(_.priority asc).map(_.source_id).result
    //val sql = sourceInfo.sortBy(_.priority asc).map(sinfo => (sinfo.source_id,sinfo.sync_finder_flag,sinfo.asyn_finder_flag,sinfo.rss_finder_flag))
    //println(sql)
    val result = exec(sql)
    result.toList
  }

  def loadSourceIdListNew: List[(Int, Boolean, Boolean, Boolean)] = {
    //    val sql = sourceInfo.sortBy(_.priority asc).map(_.source_id).result
    val sql = sourceInfo.sortBy(_.priority asc).map(sinfo => (sinfo.source_id,sinfo.sync_finder_flag,sinfo.asyn_finder_flag,sinfo.rss_finder_flag)).result
    val result = exec(sql)
    result.toList
  }

}


object SourceInfoDao {
  def apply() = {
    new SourceInfoDao()
  }
}

object SourceInfoDaoTest {
  def main(args: Array[String]) {
    val m = SourceInfoDao()
    //m.upsert(1,1,"huzyAPItest","/adatakd/jka/hdfka/",1)
    println(m.loadSourceIdListNew)
  }
}
