package com.teradata.openapi.master.controller.dao

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.{SourceInfoRow, StrategyCodeRow}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by lzf on 2016/5/12.
  */

class StrategyCodeDao extends OpenApiLogging {

    class StrategyCodeTable(tag: Tag) extends Table[StrategyCodeRow](tag, Option("opi"), "strategy_code") {

      def strategy_id = column[Int]("strategy_id", NotNull, O.AutoInc)
      def strategy_name = column[String]("strategy_name", NotNull)
      def strategy_desc = column[String]("strategy_desc", NotNull)
      def strategy_arg  = column[String]("strategy_arg", NotNull)
      def creat_time = column[Long]("creat_time", NotNull)
      def creat_persn = column[String]("creat_persn", NotNull)

      def pk = primaryKey("pk_strategy_id", (strategy_id))
      def * = (strategy_id, strategy_name, strategy_desc, strategy_arg, creat_time, creat_persn) <> (StrategyCodeRow.tupled, StrategyCodeRow.unapply)

    }

    lazy val strategyCode = TableQuery[StrategyCodeTable]
    val openapiDb: PostgresDriver.backend.DatabaseDef = Database.forConfig("postgresql")
    def exec[T](program: DBIO[T]): T = Await.result(openapiDb.run(program), 15.seconds)

    def create() = {
      val sql = strategyCode.schema.create
      val sqlInfo = strategyCode.schema.createStatements
      println(sqlInfo)
      try {
        exec(sql)
      } catch {
        case e:Exception => throw e
      }
    }

    def drop() = {
      val sql = strategyCode.schema.drop
      try {
        exec(sql)
      } catch {
        case e:Exception => throw e
      }
    }

    def loadStrategyMap: scala.collection.mutable.Map[Int, StrategyCodeRow] = {
      val sql = strategyCode.result
      val result = exec(sql)
      val strategyCodeMap = scala.collection.mutable.Map[Int, StrategyCodeRow]()
      result.foreach { e => strategyCodeMap += (e.strategy_id -> e) }
      strategyCodeMap
    }


}
