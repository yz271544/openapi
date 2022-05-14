package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.RepArg
import com.teradata.openapi.framework.step.SyncStep
import com.teradata.openapi.framework.util.pluginUtil.SqlUtil


/**
  * Created by John on 2016/8/11.
  */
class FtchSyncByJdbcOra extends SyncDbTaskPlugin[SyncStep] with OpenApiLogging {

  //取数SQL拼接
  override def driverTableSql(step: SyncStep): String = {
    val sourceTableName = step.tableInfo.tableFullName
    this.titleContext = SqlUtil.projColumns(step.repArgs)
    //s"select $titleContext from %s where 1=1 %s qualify row_number() over(order by $titleContext) between %s and %s".format(sourceTableName, PluginUtil.sourceCondition(step.source.source_id, step.reqArgs).toString, s"($page_num - 1) * $page_size + 1", s"($page_num * $page_size)")
    s"select * from (select ROWNUM AS rowno,$titleContext from %s where 1=1 %s AND ROWNUM <=%s) table_alias where  rowno>= %s".format(sourceTableName, SqlUtil.sourceCondition(step.source.source_id, step.source.source_type_code, step.reqArgs).toString, s"($page_num - 1) * $page_size + 1", s"($page_num * $page_size)")
  }

  //计数SQL拼接
  override def totalCountSql(step: SyncStep): String = {
    val sourceTableName = step.tableInfo.tableFullName
    this.titleContext = SqlUtil.projColumns(step.repArgs)
    s"select count(1) from %s where 1=1 %s".format(sourceTableName, SqlUtil.sourceCondition(step.source.source_id, step.source.source_type_code, step.reqArgs).toString)
  }

  //初始化参数
  override def init(step: SyncStep, template: String, env: Map[String, Any]): Unit = {
    sourceInfoRow = step.source
    retFormat = step.format
    retEncode = step.encode
    columnNames = for (elem: RepArg <- step.repArgs) yield elem.fieldName
    log.debug("FtchSyncByJdbc RepArgs List:" + columnNames)
    page_size = step.pageSize.getOrElse(200)
    page_num = step.pageNum.getOrElse(1)
    if (page_num < 1) page_num = 1
    //如果查询的是第1页，展示数据总共有多少行
    if (page_num == 1) {
      countSql = totalCountSql(step)
    }
    querySql = driverTableSql(step)
    println("QuerySQL:" + querySql)
    log.debug("QuerySQL:" + querySql)
  }

  //执行 def execute() 直接使用父类接口中的方法
}



