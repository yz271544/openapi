package com.teradata.openapi.master.test

import com.teradata.openapi.framework.deploy.ReqArg

/**
  * Created by John on 2016/10/18.
  */
object ReqArgsTest {
  def main(args: Array[String]) {
    val aa = List(ReqArg("TABLENAME","String",List(),1000,List("TB_4G_LAC_CELL"),0,None), ReqArg("SOURCEID","Integer",List(),1000,List("1"),0,None), ReqArg("SCHEMANAME","String",List(),1000,List("PCDE3"),0,None), ReqArg("UPDCYCLETYPE","String",List(),3002,List("ADD"),0,None))
    println(aa)
    for (elem <- aa) {
      println("argName:" + elem.fieldName + " argValue:" + elem.fieldValue)
    }
  }
}
