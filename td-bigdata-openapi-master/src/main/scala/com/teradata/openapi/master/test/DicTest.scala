package com.teradata.openapi.master.test

import com.teradata.openapi.framework.model.DataSnpst

/**
  * Created by John on 2016/12/16.
  */
object DicTest {
  def main(args: Array[String]) {
    val listDataSnpst = List(DataSnpst("DEAL_DATE",List(201611)),DataSnpst("Start_DATE",List(20161101,20161201)))
    val aa: Map[String, List[Any]] = listDataSnpst.map(i => (i.columnName,i.columnValues)).toMap
    println(aa)
  }
}
