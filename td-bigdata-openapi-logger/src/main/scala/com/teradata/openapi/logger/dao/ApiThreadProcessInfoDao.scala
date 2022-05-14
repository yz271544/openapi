package com.teradata.openapi.logger.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.logger.model.ApiThreadProcessInfo
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration._


/**
  * Created by Evan on 2016/6/14.
  */
class ApiThreadProcessInfoDao extends OpenApiLogging {

  val schemaName = "opi"
  val tableName = "api_thread_process_info"

  class ApiThreadProcessInfoTable(tag:Tag) extends Table[ApiThreadProcessInfo](tag, Option(schemaName), tableName){

    def do_time = column[String]("do_time")
    def deal_cell = column[String]("deal_cell")
    def parent_thread_id = column[String]("parent_thread_id")
    def thread_id = column[String]("thread_id")
    def contents = column[String]("contents")

    override def * : ProvenShape[ApiThreadProcessInfo] = (do_time,deal_cell,parent_thread_id,thread_id,contents) <> (ApiThreadProcessInfo.tupled,ApiThreadProcessInfo.unapply)
  }

  lazy val apiThreadProcessInfoDao  = TableQuery[ApiThreadProcessInfoTable]
  val openapidb = Database.forConfig("mypostgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapidb.run(program), 15.seconds)

  /**
    * 保存API线程处理记录信息
    * @param apiThreadProcessInfo
    * @return
    */
  def saveApiThreadProcessInfo(apiThreadProcessInfo:ApiThreadProcessInfo):Int = {
    //logInfo("开始保存API线程处理记录信息")
    exec(apiThreadProcessInfoDao += apiThreadProcessInfo)
  }


}
