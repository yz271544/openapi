package com.teradata.openapi.master.test

import com.teradata.openapi.framework.model.{DataSnpst, DataSnpstVal}
import org.apache.commons.collections.map.LinkedMap

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by John on 2016/7/30.
  */
object TestList {
  def main(args: Array[String]) {
    /*var listBf = ListBuffer("a","b","c")
    listBf = listBf.-("b")
    println(listBf)

    listBf = listBf += "D"
    println("+=:" + listBf)

    val aa = DataSnpstVal(List())
    println(aa.dataSnpstVal.isEmpty)*/

    /*val list = List(1,2,3,4)

    val retListBuffer: ListBuffer[Int] = ListBuffer[Int]()
    for (elem <- list) {
      retListBuffer.+=(elem)
    }
    println(retListBuffer.toList)*/

    /*val lat = List(List())
    println(lat.flatten)
    println(lat.flatten.isEmpty)*/

    /*var ts = ListBuffer(DataSnpst("DEAL_DATE",List(201605)),DataSnpst("DEAL_DATE",List(201606)))
    println(ts)
    println(ts.indexOf(DataSnpst("DEAL_DATE",List(201606))))
    ts.drop(ts.indexOf(DataSnpst("DEAL_DATE",List(201606))))
    println(ts)

    ts = ts - DataSnpst("DEAL_DATE",List(201606))
    println(ts)

    ts += DataSnpst("DEAL_DATE",List(201607))

    println(ts)
    */

    //  val findRet: List[DataSnpst] = List(DataSnpst("DEAL_DATE",List(201601,201602)),DataSnpst("REGION_CODE",List("01","02","03","04")))
    val findRet: List[DataSnpst] = List(DataSnpst("DEAL_DATE", List(1)), DataSnpst("REGION_CODE", List()))
    val isNull: Boolean = findRet.map(_.columnValues.isEmpty).aggregate(true)(_ & _, _ & _)

    //println(isNull)


    val test = List(DataSnpst("DEAL_DATE",List()))
    //println(isNullFlag(test))
  }
  def isNullFlag(findRet: List[DataSnpst]): Boolean = {
    val isNullFlag = findRet.map(_.columnValues.isEmpty).aggregate(true)(_ & _, _ & _)
    isNullFlag
  }

  val tBooList = List(false,true,false)
  val aggrBooVal = tBooList.aggregate(true)(_ & _, _ & _)
  println("aggrBooVal:" + aggrBooVal)

  val foldBooValStr = tBooList.fold(true.toString)(_.toString + " & " + _.toString)
  println("foldBooValStr:" + foldBooValStr)

  val foldBooVal = tBooList.fold(true)(_ & _)
  println("foldBooVal:" + foldBooVal)


  val linkmap = new LinkedMap()
  linkmap.put("DEAL_DATE",List(201601))
  linkmap.put("REGION_CODE",List("01","02","03"))

  println("LINKEDmAP:" + linkmap)

  val linkHashMap = mutable.LinkedHashMap

}
