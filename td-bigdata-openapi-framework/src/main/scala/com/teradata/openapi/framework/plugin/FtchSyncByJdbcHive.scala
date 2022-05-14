package com.teradata.openapi.framework.plugin


import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.step.SyncStep
import com.teradata.openapi.framework.util.pluginUtil._

/**
  * Created by John on 2016/8/15.
  */
class FtchSyncByJdbcHive extends SyncDbTaskPlugin[SyncStep] with OpenApiLogging {

  def tableCovHiveTableName(tablename: String): String = {
    PluginEnv.plugCachSchema + "." + tablename.replace(".", "_")
  }

  def getCondition(source_type_code: String, reqArgs: List[ReqArg]): String = {
    val s = TypeConvertUtil.reqCvtCondition(source_type_code) _
    //println("s function is :" + s)
    var r: String = ""
    log.debug("---------------------source_id is :" + source_type_code)
    log.debug("---------------------reqList is :" + reqArgs)
    for (elem <- reqArgs) {
      val fun = s(elem.fieldValue)
      //println("fun function is :" + fun)
      //println("elem is " + elem)
      //println("source_id is " + source_id)
      r += " and " + elem.fieldName + " in " + fun(elem.fieldTargtType)
    }
    r
  }

  override def driverTableSql(step: SyncStep): String = {
    this.titleContext = SqlUtil.projColumns(step.repArgs)
    s"SELECT xx.* FROM(SELECT $titleContext,row_number() over(ORDER BY $titleContext)AS num FROM %s where 1=1 %s) xx WHERE num BETWEEN %s AND %s".format(tableCovHiveTableName(step.tableInfo.tableFullName), this.getCondition(step.source.source_type_code, step.reqArgs).toString,s"($page_num - 1) * $page_size + 1",s"($page_num * $page_size)")
  }

  //计数SQL拼接
  override def totalCountSql(step: SyncStep): String = {
    val sourceTableName = step.tableInfo.tableFullName
    this.titleContext = SqlUtil.projColumns(step.repArgs)
    s"select count(1) from %s where 1=1 %s".format(tableCovHiveTableName(step.tableInfo.tableFullName), this.getCondition(step.source.source_type_code, step.reqArgs).toString)
  }

  //初始化参数
  override def init(step: SyncStep, template: String, env: Map[String, Any]): Unit = {
    sourceInfoRow = step.source
    retFormat = step.format
    retEncode = step.encode
    columnNames = for (elem <- step.repArgs) yield elem.fieldName
    page_size = step.pageSize.getOrElse(200)
    page_num = step.pageNum.getOrElse(1)
    if (page_num < 1) page_num = 1
    //如果查询的是第1页，展示数据总共有多少行
    if (page_num == 1) {
      countSql = totalCountSql(step)
      //println("countSql:" + countSql)
    }
    querySql = driverTableSql(step)
    //println("QuerySQL:" + querySql)
    log.debug("QuerySQL:" + querySql)
  }

  //执行 def execute() 直接使用父类接口中的方法
}


