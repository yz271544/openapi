package com.teradata.openapi.logger.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.logger.model.ApiThreadProcessRelation
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by Evan on 2016/6/14.
  */
class ApiThreadProcessRelationDao extends OpenApiLogging {

  val schemaName = "opi"
  val tableName = "api_thread_process_relation"

  class ApiThreadProcessRelationTable(tag:Tag) extends Table[ApiThreadProcessRelation](tag, Option(schemaName), tableName){

    def sent_thread_id = column[String]("sent_thread_id")
    def recv_thread_id = column[String]("recv_thread_id")

    override def * : ProvenShape[ApiThreadProcessRelation] = (sent_thread_id,recv_thread_id) <> (ApiThreadProcessRelation.tupled,ApiThreadProcessRelation.unapply)
  }

  lazy val apiThreadProcessRelationDao = TableQuery[ApiThreadProcessRelationTable]
  val openapidb = Database.forConfig("mypostgresql")
  def exec[T](program: DBIO[T]): T = Await.result(openapidb.run(program), 15.seconds)

  /**
    *
    * @param apiThreadProcessRelation
    * @return
    */
  def saveApiThreadProcessRelation(apiThreadProcessRelation:ApiThreadProcessRelation):Int = {
    //logInfo("开始保存API线程关系信息")
    exec(apiThreadProcessRelationDao += apiThreadProcessRelation)
  }
}
