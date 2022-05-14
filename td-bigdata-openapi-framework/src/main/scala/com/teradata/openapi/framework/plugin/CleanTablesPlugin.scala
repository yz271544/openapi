package com.teradata.openapi.framework.plugin

import java.sql.Connection

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.CacheTable
import com.teradata.openapi.framework.step.CleanStep
import com.teradata.openapi.framework.util.pluginUtil.{PluginEnv, SqlUtil}

/**
  * Created by hdfs on 2016/7/8.
  */
class CleanTablesPlugin extends AsyncTaskPlugin[CleanStep] with OpenApiLogging {
  private var cacheTables: List[CacheTable] = _
  private var template: String = _

  def doSqlConn[T](f: Connection => T): T = {
    var conn: Connection = null
    try {
//      conn = SqlUtil.getConnection(PluginEnv.plugCacheHiveSourceInfo)
      conn = SqlUtil.getConnection(PluginEnv.plugCachHiveSourceInfo)
      f(conn)
    } catch {
      case ex: Exception =>
        log.debug("CleanTbs [" + ex.getMessage + "]")
        throw ex
    } finally {
      if (conn != null) {
        conn.close()
      }
    }
  }

  def deleteTables(): Unit = {
    import scala.collection.mutable.ListBuffer
    this.cacheTables.foreach {
      cacheTable =>
        val schemaName = cacheTable.schemaName
        val tableName = cacheTable.tableName
        val columnMap = cacheTable.columnMap
        val sqlBuff = new ListBuffer[String]
        var partitionFlag = false
        if (columnMap.isEmpty) {
          sqlBuff.append("drop table " + schemaName + "." + tableName)
        }
        else {
          log.debug("show partitions [" + schemaName + "." + tableName + "]")
          doSqlConn { conn =>
            try {
              val stmt = conn.createStatement()
              val res = stmt.executeQuery("show partitions" + schemaName + "." + tableName)
              if (res.next()) {
                partitionFlag = true
              }
            } catch {
              case ex: Exception =>
                val exMsg=ex.getMessage
                log.debug("show partitions Exception" + schemaName + "." + tableName + " [" + exMsg + "]")
            }
          }

          for ((filed, filedValueList) <- columnMap) {
            for (filedValue <- filedValueList) {
              if (filedValueList.getClass == classOf[Int]) {
                if (partitionFlag) {
                  sqlBuff.append("ALTER TABLE " + schemaName + "." + tableName + " DROP IF EXISTS PARTITION( " + filed + "=" + filedValue + ")")
                } else {
                  sqlBuff.append("DELETE FROM " + schemaName + "." + tableName + " WHERE " + filed + "=" + filedValue)
                }
              }
              else {
                if (partitionFlag) {
                  sqlBuff.append("ALTER TABLE " + schemaName + "." + tableName + " DROP IF EXISTS PARTITION( " + filed + "='" + filedValue + "')")
                } else {
                  sqlBuff.append("DELETE FROM " + schemaName + "." + tableName + " WHERE " + filed + "='" + filedValue + "'")
                }

              }
            }
          }
        }
        log.debug("CleanTbs [" + sqlBuff.toString + "]")
        doSqlConn { conn =>
          for (sql <- sqlBuff) {
            if (!sql.isEmpty) {
              println("execute ddl  SQL :" + sql)
              try {
                val stmt = conn.createStatement()
                stmt.execute(sql)
              } catch {
                case ex: Exception =>
                  log.debug("CleanTb Exception [" + sql + "] [" + ex.getMessage + "]")
              }
            }
          }
        }
    }
  }

  override def init(step: CleanStep, template: String, env: Map[String, Any]): Unit = {
    log.debug("CleanTbs Plugin's initiation function is starting ...")
    this.cacheTables = step.cleanTables
    this.template = template
    log.debug("CleanTbs Plugin's initiation function is finished ...")
  }

  override def execute(): Int = {
    log.debug("CleanTbs Plugin's exceute function is starting... ")
    try {
      deleteTables()
    } catch {
      case e: Exception =>
        log.warn("Couldn't del table  ." + e.getMessage)
        throw e
    }
    log.debug("CleanTbs Plugin's exceute function is finished... ")
    0
  }
}
