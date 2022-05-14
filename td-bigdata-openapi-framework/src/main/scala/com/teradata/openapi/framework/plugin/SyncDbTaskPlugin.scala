package com.teradata.openapi.framework.plugin

import java.sql.{Connection, PreparedStatement, ResultSet}

import com.teradata.openapi.framework.deploy.{Encode, Format, FormatType}
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.pluginUtil.{SqlUtil, TypeConvertUtil}

/**
  * Created by lzf on 2016/4/12.
  */
trait SyncDbTaskPlugin[SyncStep] extends SyncTaskPlugin[SyncStep] {
  //数据源
  var sourceInfoRow: SourceInfoRow = _
  //数据title
  var titleContext: String = _
  //取数查询SQL
  var querySql: String = _
  //返回编码
  var retEncode: Encode.Value = _
  //返回格式
  var retFormat: Format = _
  //字段名称列表
  var columnNames: List[String] = _
  //总计记录数查询SQL
  var countSql: String = _
  //总计记录数
  var totalCount: Int = _
  //分页展示多少行
  var page_size: Int = _
  //查询第几页
  var page_num: Int = _

  //取数SQL拼接
  def driverTableSql(step: SyncStep): String

  //计数SQL拼接
  def totalCountSql(step: SyncStep): String

  def init(step: SyncStep, template: String, env: Map[String, Any])

  def execute(): String = {
    var conn: Connection = null
    var statement: PreparedStatement = null
    var retSet: ResultSet = null
    var result: String = null
    try {
      conn = SqlUtil.getConnection(sourceInfoRow)
      if (countSql != null) {
        var countRetSet: ResultSet = null
        var countStatment: PreparedStatement = null
        try {
          countStatment = conn.prepareStatement(countSql)
          countRetSet = countStatment.executeQuery()
          totalCount = if (countRetSet.next()) countRetSet.getInt(1) else 0
        } catch {
          case e: Exception =>
        } finally {
          SqlUtil.closeResultSet(countRetSet)
          SqlUtil.closeStatement(countStatment)
        }
      }
      statement = conn.prepareStatement(querySql)
      retSet = statement.executeQuery()
      result = if (retFormat.formType.toUpperCase equals FormatType.JSON.toString)
        TypeConvertUtil.resultSetToJson(columnNames, retSet, page_num, page_size, totalCount)
      else if(retFormat.formType.toUpperCase equals FormatType.TXT.toString)
        TypeConvertUtil.resultSetToTxt(columnNames, retSet, page_num, page_size, totalCount)
      else TypeConvertUtil.resultSetToXml(columnNames, retSet, page_num, page_size, totalCount)
    } catch {
      case e: Exception => throw e
    } finally {
      SqlUtil.close(conn, statement, retSet)
    }
    if (result == null) result = "NO DATA!"
    result
  }
  override def toString = {
    "SyncTaskPlugin"
  }
}


