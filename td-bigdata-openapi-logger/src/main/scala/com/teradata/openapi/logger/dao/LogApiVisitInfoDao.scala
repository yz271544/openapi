package com.teradata.openapi.logger.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.logger.model.LogApiVisitInfo
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by Evan on 2016/6/13.
  */
class LogApiVisitInfoDao extends OpenApiLogging {

  val schemaName = "opi"
  val tableName = "api_visit_info"

  class LogApiVisitInfoTable(tag:Tag) extends Table[LogApiVisitInfo](tag, Option(schemaName), tableName){

    def do_time = column[String]("do_time")
    def app_key = column[String]("app_key")
    def call_way = column[String]("call_way")
    def req_type = column[String]("req_type")
    def version = column[String]("version")
    def thread_id = column[String]("thread_id")

    override def * : ProvenShape[LogApiVisitInfo] = (do_time,app_key,call_way,req_type,version,thread_id) <> (LogApiVisitInfo.tupled,LogApiVisitInfo.unapply)
  }

  lazy val logApiVisitInfoDao = TableQuery[LogApiVisitInfoTable]
  val openapidb = Database.forConfig("mypostgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapidb.run(program), 15.seconds)

  /**
    * 保存API访问记录信息
    * @param logApiVisitInfo
    * @return
    */
  def saveLogApiVisitInfo(logApiVisitInfo:LogApiVisitInfo):Int = {
    //logInfo("开始保存访问记录信息")
    exec(logApiVisitInfoDao += logApiVisitInfo)
  }

}
