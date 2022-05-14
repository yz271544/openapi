package com.teradata.openapi.logger.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.logger.model.{LogApiVisitInfo, LogDetailInfo}
import slick.driver.PostgresDriver.api._
//import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by Evan on 2016/6/7.
  */
class LogDetailInfoDao extends OpenApiLogging{

  val schemaName = "opi"
  val tableName = "log_info_detail"

  class LogDetailInfoTable(tag:Tag) extends Table[LogDetailInfo](tag, Option(schemaName), tableName){

    def do_time = column[String]("do_time")
    def mod_name = column[String]("mod_name")
    def lvl = column[String]("lvl")
    def class_name = column[String]("class_name")
    def thread_name = column[String]("thread_name")
    def thread_id = column[String]("thread_id")
    def contents = column[String]("contents")

    override def * : ProvenShape[LogDetailInfo] = (do_time,mod_name,lvl,class_name,thread_name,thread_id,contents) <> (LogDetailInfo.tupled,LogDetailInfo.unapply)
  }

  lazy val logDetailInfoDao = TableQuery[LogDetailInfoTable]
  val openapidb = Database.forConfig("mypostgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapidb.run(program), 15.seconds)

  /**
    * 同时保存多条日志清单
    * @param logDetailInfos
    * @return
    */
  def saveMultiLogDetailInfo(logDetailInfos:Seq[LogDetailInfo]):Option[Int] = {
    //logInfo("开始保存日志信息记录")
    exec(logDetailInfoDao ++= logDetailInfos)
  }

  /**
    * 保存日志信息
    * @param logDetailInfo
    * @return
    */
  def saveLogDetailInfo(logDetailInfo:LogDetailInfo):Int = {

    //logInfo("开始保存日志信息记录")
    exec(logDetailInfoDao += logDetailInfo)

  }



}






















