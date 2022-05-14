package com.teradata.openapi.master.finder.tools

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.deploy.Master

import scala.collection.immutable.TreeMap

/**
  * Created by John on 2016/7/12.
  */
class CycleRcd extends ToolApis with DicMapFunc with OpenApiLogging {
  override def execute(reqID: String, master: Master, reqArgs: List[ReqArg]) = {
    val ret = new FindToReqAsynYIK()

    log.debug("reqArgs:" + reqArgs)
    println("reqArgs:" + reqArgs)

    for(elem: ReqArg <- reqArgs) {
      log.debug("argName:" + elem.fieldName + " argValue:" + elem.fieldValue)
      println("argName:" + elem.fieldName + " argValue:" + elem.fieldValue)
    }
    val sourceid: Int = (for (elem <- reqArgs if elem.fieldName equals "SOURCEID") yield elem.fieldValue).flatten.head.toString.toInt
    val schemaName: String = (for (elem <- reqArgs if elem.fieldName equals "SCHEMANAME") yield elem.fieldValue).flatten.head.toString
    val tableName: String = (for (elem <- reqArgs if elem.fieldName equals "TABLENAME") yield elem.fieldValue).flatten.head.toString
    val cycleColumn: String = (for (elem <- reqArgs if elem.fieldName equals "CYCLECOLUMN") yield elem.fieldValue).flatten.headOption.getOrElse("").toString
    val cycleValus: List[Any] = (for (elem <- reqArgs if elem.fieldName equals "CYCLEVALUS") yield elem.fieldValue).flatten
    val updCycleType: String = (for (elem <- reqArgs if elem.fieldName equals "UPDCYCLETYPE") yield elem.fieldValue).flatten.head.toString
    ret.setReqID(reqID)
    log.debug("cycle info:" + cycleColumn + " value:" + cycleValus)
    val jsonCondtionMap: TreeMap[String, List[Any]] = if (!("" equals cycleColumn)) TreeMap(cycleColumn -> cycleValus) else TreeMap()
    val finderElements = Json.generate(jsonCondtionMap)
    log.info("finderElements:" + finderElements)
    master.finderActor ! UpdCycleRcdToFinder(reqID ,master.finderActor, UpdCycleRcd(sourceid, schemaName, tableName, finderElements, updCycleType))
    if (updCycleType.toUpperCase equals "ADD") {
      ret.setRetMsg("ADD[" + sourceid + "]" + schemaName + "." + tableName + " ----> " + jsonCondtionMap)
      log.info("FindCycRcdToSchedule[" + sourceid + "]" + schemaName + "." + tableName + " ----> " + jsonCondtionMap)
      ret.setRetCode("0")
      ret.setReqStat("ADD Snapshot Successed.")
    }
    else if (updCycleType.toUpperCase equals "DROP") {
      ret.setRetMsg("DROP[" + sourceid + "]" + schemaName + "." + tableName + " ----> " + jsonCondtionMap)
      ret.setRetCode("0")
      ret.setReqStat("DROP Snapshot Successed.")
    }
    else {
      ret.setRetMsg("ERROR Param[" + sourceid + "]" + schemaName + "." + tableName + " ----> " + jsonCondtionMap + " updCycleType:" + updCycleType.toUpperCase)
      ret.setRetCode("-1")
      ret.setReqStat("Param updCycleType Error!...")
    }
    ret
  }
}

/*object CycleRcd {
  def main(args: Array[String]) {

    val reqid = "0"
    val rq1 = ReqArg("SOURCEID", "Integer", List[SorcType](), 1000, List(0), 0)
    val rq2 = ReqArg("SCHEMANAME", "String", List[SorcType](), 1000, List("RPTMART3"), 0)
    val rq3 = ReqArg("TABLENAME", "String", List[SorcType](), 1000, List("TB_RPT_BO_MON1"), 0)
    val rq4 = ReqArg("CYCLECOLUMN", "String", List[SorcType](), 1000, List("DEAL_DATE"), 0)
    val rq5 = ReqArg("CYCLEVALUS", "Integer", List[SorcType](), 1000, List(201604,201605), 0)
    val rq6 = ReqArg("UPDCYCLETYPE", "String", List[SorcType](), 1000, List("ADD"), 0)
    val reqargs = List(rq1,rq2,rq3,rq4,rq5,rq6)

    val a = new CycleRcd
    //a.execute(reqid,reqargs)
  }
}*/
