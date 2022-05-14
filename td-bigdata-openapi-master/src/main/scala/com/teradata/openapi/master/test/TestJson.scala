package com.teradata.openapi.master.test

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.model.{DataSnpst, DataSnpstVal, ListDataSnpst}
import com.teradata.openapi.framework.util.DicMapFunc

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

/**
  * Created by John on 2016/7/29.
  */
object TestJson extends DicMapFunc {
  def main(args: Array[String]) {


    /*
        //val old = """{"dataSnpstVal":[{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
        //"""{"dataSnpstVal":[{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
        val old = """{"dataSnpstVal":[]}"""
        //val old = """{}""" // ERROR
        val oldJson2Obj: DataSnpstVal = Json.parse[DataSnpstVal](old)
        //val oldJson2Obj: DataSnpstVal = DataSnpstVal(List[ListDataSnpst]())
        println("oldJson2Obj:" + oldJson2Obj)


        val addVal: DataSnpst = DataSnpst("START_DATE", List(201608,201609))
        //val addVal2:  DataSnpst = DataSnpst("REGION_CODE", List("01","00"))
        //val vvv: ListDataSnpst = ListDataSnpst(List(addVal,addVal2))
        val vvv: ListDataSnpst = ListDataSnpst(List(addVal))
        val ccc: List[DataSnpst] = vvv.listdataSnpstDic


        val dd: List[ListDataSnpst] = DataSnpstVal(oldJson2Obj.dataSnpstVal).add(ccc)

        println("DDDDDD:" + dd)*/


    //val d = DataSnpst("DEAL_DATE", List(201601))

    //println("EQU:" + a.equals(d))

    /*val a = DataSnpst("DEAL_DATE", List(201604,201605))
    val listA = ListDataSnpst(List(a))



    val b = DataSnpst("DEAL_DATE", List(201606, 201607))
    val c = DataSnpst("REGION_CODE", List("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"))
    val listB = ListDataSnpst(List(b, c))


    val lll = DataSnpstVal(List(listA,listB))


    println("lll:" + lll)
    println("JSON:" + Json.generate(lll))*/


    /*val lll = DataSnpstVal(List[ListDataSnpst]())
    println("JSON:" + Json.generate(lll))*/

    /*val valA = List(a)
    val valB = List(b, c)*/

    /*val valC = valA +: valB

    println("valC:" + valC)*/

    /*println(Json.generate(List(listA)))
    println(Json.generate(List(listB)))*/

    //------------DROP-----------------------------
    val old = """{"dataSnpstVal":[{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
    //"""{"dataSnpstVal":[{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
    //val old = """{"dataSnpstVal":[]}"""
    //val old = """{}""" // ERROR
    val oldJson2Obj: DataSnpstVal = Json.parse[DataSnpstVal](old)
    //val oldJson2Obj: DataSnpstVal = DataSnpstVal(List[ListDataSnpst]())
    println("oldJson2Obj:" + oldJson2Obj)


    val addVal: DataSnpst = DataSnpst("DEAL_DATE", List(201606))
    val addVal2:  DataSnpst = DataSnpst("REGION_CODE", List("01","02"))
    val vvv: ListDataSnpst = ListDataSnpst(List(addVal,addVal2))
    //val vvv: ListDataSnpst = ListDataSnpst(List(addVal))
    val ccc: List[DataSnpst] = vvv.listdataSnpstDic


    val dd: List[ListDataSnpst] = DataSnpstVal(oldJson2Obj.dataSnpstVal).drop(ccc)

    println("DROPPPPPPP::1::" + dd)

/*
    val addVal1: DataSnpst = DataSnpst("DEAL_DATE", List(201606,201607))
    val addVal2:  DataSnpst = DataSnpst("REGION_CODE", List("01","00"))
    //val vvv: ListDataSnpst = ListDataSnpst(List(addVal,addVal2))
    val vvv1: ListDataSnpst = ListDataSnpst(List(addVal1,addVal2))
    val ccc1: List[DataSnpst] = vvv1.listdataSnpstDic


    val dd1: List[ListDataSnpst] = DataSnpstVal(dd).drop(ccc1)

    println("DROPPPPPPP::2::" + dd1)

    val addVal1: DataSnpst = DataSnpst("DEAL_DATE", List(201606,201607))
    val addVal2:  DataSnpst = DataSnpst("REGION_CODE", List("01","00"))
    //val vvv: ListDataSnpst = ListDataSnpst(List(addVal,addVal2))
    val vvv1: ListDataSnpst = ListDataSnpst(List(addVal1,addVal2))
    val ccc1: List[DataSnpst] = vvv1.listdataSnpstDic


    val dd1: List[ListDataSnpst] = DataSnpstVal(oldJson2Obj.dataSnpstVal).drop(ccc1)

    println("DROPPPPPPP::2::" + dd1)*/
    //-------------------------------------------


    //------------find-----------------------------
    /*val old =
      """{"dataSnpstVal":[{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpstDic":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
    //"""{"dataSnpstVal":[{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201604,201605]}]},{"listdataSnpst":[{"columnName":"DEAL_DATE","dataSnpstVal":[201606,201607]},{"columnName":"REGION_CODE","dataSnpstVal":["01","02","03","04","05","06","07","08","09","10","11"]}]}]}"""
    //val old = """{"dataSnpstVal":[]}"""
    //val old = """{}""" // ERROR
    val oldJson2Obj: DataSnpstVal = Json.parse[DataSnpstVal](old)
    //val oldJson2Obj: DataSnpstVal = DataSnpstVal(List[ListDataSnpst]())
    println("oldJson2Obj:" + oldJson2Obj)


    val addVal: DataSnpst = DataSnpst("DEAL_DATE", List(201609))
    val addVal2: DataSnpst = DataSnpst("REGION_CODE", List("01"))
    val vvv: ListDataSnpst = ListDataSnpst(List(addVal, addVal2))
    //val vvv: ListDataSnpst = ListDataSnpst(List(addVal))
    //val ccc: List[DataSnpst] = vvv.listdataSnpstDic


    val dd: (List[DataSnpst], List[DataSnpst]) = DataSnpstVal(oldJson2Obj.dataSnpstVal).find(vvv)

    println("Founded::1::" + dd._1)
    println("UnFounded::1::" + dd._2)
    val findRet: List[DataSnpst] = dd._2
    var isFindFlag = true
    for (elem <- findRet) {
      isFindFlag = isFindFlag & (if (elem.dataSnpstVal.isEmpty) true else false)
    }
    println("isFindFlag:" + isFindFlag)*/
    /*println
    println
    println
    println
    println


    val sorcList = List(List(201601, "00"), List(201601, "01"), List(201601, "02"), List(201601, "03"), List(201602, "00"), List(201602, "01"), List(201602, "02"), List(201602, "03"), List(201603, "00"), List(201603, "01"), List(201603, "02"), List(201603, "03"))
    val dMap = TreeMap("deal_date" -> List(201601, 201602, 201603), "region_code" -> List("00", "01", "02", "03"))
    val rst: TreeMap[String, Any] = redistributeMap(sorcList,dMap)

    println(rst)*/

    /*val d: DataSnpstVal = DataSnpstVal(List(ListDataSnpst(List(DataSnpst("DEAL_DATE",List(201604)))), ListDataSnpst(List(DataSnpst("DEAL_DATE",List(201606, 201607)), DataSnpst("REGION_CODE",List("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"))))))
    val jsd = Json.generate(d)
    println("ObjToJson:"+ jsd)
    val obj = Json.parse[DataSnpstVal](jsd)
    println("JsonToObj:" + obj)*/
  }
}
