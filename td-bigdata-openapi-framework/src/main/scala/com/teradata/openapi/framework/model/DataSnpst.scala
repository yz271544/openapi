package com.teradata.openapi.framework.model

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.util.DicMapFunc

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

/**
  * 存放字段和值
  * Created by John on 2016/6/29.
  *
  */
case class DataSnpst(columnName: String, columnValues: List[Any]) {

  def drop(dataSnpst: DataSnpst): DataSnpst = {
    var retDataSnpstVal: List[Any] = List[Any]()
    if (dataSnpst.columnName.toUpperCase equals this.columnName.toUpperCase) {
      retDataSnpstVal = (this.columnValues diff dataSnpst.columnValues).distinct
    }
    DataSnpst(dataSnpst.columnName.toUpperCase, retDataSnpstVal)
  }


  def add(dataSnpst: DataSnpst): DataSnpst = {
    var retDataSnpstVal: List[Any] = List[Any]()
    if (dataSnpst.columnName.toUpperCase equals this.columnName.toUpperCase) {
      retDataSnpstVal = (this.columnValues union dataSnpst.columnValues).distinct
    }
    DataSnpst(dataSnpst.columnName.toUpperCase, retDataSnpstVal)
  }

  def getEqualVals(other: Any): List[Any] = other match {
    case that: DataSnpst =>
      (that.columnValues intersect this.columnValues).distinct
    case _ => List()
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[DataSnpst]

  override def equals(other: Any): Boolean = other match {
    case that: DataSnpst =>
      (that canEqual this) &&
        columnName.toUpperCase == that.columnName.toUpperCase &&
        (columnValues diff that.columnValues).isEmpty &&
        (that.columnValues diff columnValues).isEmpty
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(columnName)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

/**
  * 存放快照List
  *
  * @param listdataSnpstDic
  */
case class ListDataSnpst(var listdataSnpstDic: List[DataSnpst]) extends DicMapFunc with OpenApiLogging {

  def list1ExtKey2List(list: List[DataSnpst]): List[String] = {
    val retListBuffer: ListBuffer[String] = ListBuffer[String]()
    for (elem <- list) {
      retListBuffer.+=(elem.columnName)
    }
    retListBuffer.toList
  }

  def compareDataSnpstKeyEquals(list1: List[DataSnpst], list2: List[DataSnpst]): Boolean = {
    val list1ExtKeyList: List[String] = list1ExtKey2List(list1)
    val list2ExtKeyList: List[String] = list1ExtKey2List(list2)
    if ((list1ExtKeyList diff list2ExtKeyList).isEmpty && (list2ExtKeyList diff list1ExtKeyList).isEmpty) true else false
  }

  def add(dataSnpstList: List[DataSnpst]): (Boolean, List[DataSnpst]) = {
    var retListdataSnpst: List[DataSnpst] = List[DataSnpst]()
    val isMatch: Boolean = compareDataSnpstKeyEquals(this.listdataSnpstDic, dataSnpstList)
    if (isMatch) {
      //println("ListDataSnpst DIC:[" + this.listdataSnpstDic + "]")
      //println("ListDataSnpst ADD:[" + dataSnpstList + "]")
      for (elemDataSnpstDic: DataSnpst <- this.listdataSnpstDic; elemDataSnpst <- dataSnpstList) {
        val result: DataSnpst = elemDataSnpstDic.add(elemDataSnpst)
        if (result.columnValues.nonEmpty) {
          //println("FOR IN AA:" + result)
          retListdataSnpst = retListdataSnpst.::(result)
        }
      }
    } else {
      retListdataSnpst = this.listdataSnpstDic
      //println("ELSE IN retListdataSnpst:" + retListdataSnpst)
    }
    //println("retListdataSnpst:" + retListdataSnpst)
    (isMatch, retListdataSnpst)
  }

  def drop(dropDataSnpstList: List[DataSnpst]): List[DataSnpst] = {
    var retListdataSnpst: List[DataSnpst] = List[DataSnpst]()
    val isMatch: Boolean = compareDataSnpstKeyEquals(this.listdataSnpstDic, dropDataSnpstList)
    println("dataSnpstList.isMatch:" + isMatch)
    if (isMatch) {
      //println("ListDataSnpst DIC:[" + this.listdataSnpstDic + "]")
      //println("ListDataSnpst DROP:[" + dataSnpstList + "]")

      val dicDataSnpstMap: TreeMap[String, List[Any]] = listDataSnpst2TreeMap(this.listdataSnpstDic)
      println("ListDataSnpst :dicDataSnpstMap: " + dicDataSnpstMap)
      val dicDataSnpstMap2Cartesian: List[List[Any]] = cartesianProductStr(dicDataSnpstMap.keySet.map(dicDataSnpstMap).toList)
      println("ListDataSnpst:dicDataSnpstMap2Cartesian: " + dicDataSnpstMap2Cartesian)
      val findDataSnpstMap: TreeMap[String, List[Any]] = listDataSnpst2TreeMap(dropDataSnpstList)
      println("ListDataSnpst:findDataSnpstMap: " + findDataSnpstMap)
      val findDataSnpstMap2Cartesian: List[List[Any]] = cartesianProductStr(findDataSnpstMap.keySet.map(findDataSnpstMap).toList)
      println("ListDataSnpst:findDataSnpstMap2Cartesian: " + findDataSnpstMap2Cartesian)

      val dropAfterList: List[List[Any]] = dicDataSnpstMap2Cartesian diff findDataSnpstMap2Cartesian

      println("dropAfterList:" + dropAfterList)

      val dropAfterMap: TreeMap[String, Any] = redistributeMap(dropAfterList,findDataSnpstMap)
      println("dropAfterMap:" + dropAfterMap)
      val dropAfterDataSnpstList: List[DataSnpst] = treeMap2ListDataSnpst(mapToTreeMapVallst(dropAfterMap))
      println("::::::::::::::::dropAfterDataSnpstList::::::::::::::::::::::::")
      println(dropAfterDataSnpstList)
      println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
      for (elemDataSnpstDic: DataSnpst <- this.listdataSnpstDic; elemDataSnpst <- dropDataSnpstList) {
        val result: DataSnpst = elemDataSnpstDic.drop(elemDataSnpst)
        if (result.columnValues.nonEmpty) {
          //println("FOR IN AA:" + result)
          retListdataSnpst = retListdataSnpst.::(result)
        } else {
          //如果为空，多字段的如果有1个被删除为空，则整个List[DataSnpst]()被清除
          return List[DataSnpst]()
        }
      }
    } else {
      retListdataSnpst = this.listdataSnpstDic
      //println("ELSE IN retListdataSnpst:" + retListdataSnpst)
    }
    //println("retListdataSnpst:ListDataSnpst [" + retListdataSnpst + "]")
    retListdataSnpst
  }

  def find(findDataSnpstList: List[DataSnpst]): (List[List[Any]], List[List[Any]]) = {
    println("ListDataSnpst:FIND func: " + findDataSnpstList)

    val dicDataSnpstMap: TreeMap[String, List[Any]] = listDataSnpst2TreeMap(this.listdataSnpstDic)
    println("ListDataSnpst:dicDataSnpstMap: " + dicDataSnpstMap)

    val dicDataSnpstMap2Cartesian: List[List[Any]] = cartesianProductStr(dicDataSnpstMap.keySet.map(dicDataSnpstMap).toList)
    println("ListDataSnpst:dicDataSnpstMap2Cartesian: " + dicDataSnpstMap2Cartesian)

    val findDataSnpstMap: TreeMap[String, List[Any]] = listDataSnpst2TreeMap(findDataSnpstList)
    println("ListDataSnpst:findDataSnpstMap: " + findDataSnpstMap)

    val findDataSnpstMap2Cartesian: List[List[Any]] = cartesianProductStr(findDataSnpstMap.keySet.map(findDataSnpstMap).toList)
    println("ListDataSnpst:findDataSnpstMap2Cartesian: " + findDataSnpstMap2Cartesian)

    for (elem <- dicDataSnpstMap2Cartesian) {
      for (elem <- elem) {
        log.debug("Dic Data Type: " + elem.getClass)
      }
    }
    for (elem <- findDataSnpstMap2Cartesian) {
      for (elem <- elem) {
        log.debug("Find Data Type: " + elem.getClass)
      }
    }


    (findDataSnpstMap2Cartesian intersect dicDataSnpstMap2Cartesian, findDataSnpstMap2Cartesian diff dicDataSnpstMap2Cartesian)
  }

  def listValEqual(thisListdataSnpst: List[DataSnpst], thatListdataSnpst: List[DataSnpst]): Boolean = {
    var eq = true
    if (thisListdataSnpst.length == thatListdataSnpst.length) {
      for (elem: DataSnpst <- thisListdataSnpst) {
        if (!thatListdataSnpst.contains(elem)) {
          eq = false
          return eq
        }
      }
      for (elem <- thatListdataSnpst) {
        if (!thisListdataSnpst.contains(elem)) {
          eq = false
          return eq
        }
      }
    }
    else {
      eq = false
    }
    eq
  }
}


/**
  * 存放存储数据库的结构
  *
  * @param dataSnpstVal
  */
case class DataSnpstVal(dataSnpstVal: List[ListDataSnpst]) extends DicMapFunc {
  def add(dataSnpstList: List[DataSnpst]) = {
    //println("DataSnpstVal DIC:[dataSnpstVal:" + this.dataSnpstVal +"]")
    //println("DataSnpstVal ADD dataSnpstList:" + dataSnpstList + "]")
    var retDataSnpstVal: List[ListDataSnpst] = List[ListDataSnpst]()
    var isMatch = true
    for (elem: ListDataSnpst <- this.dataSnpstVal) {
      val result: (Boolean, List[DataSnpst]) = elem.add(dataSnpstList)
      retDataSnpstVal = retDataSnpstVal.::(ListDataSnpst(result._2))
      isMatch = isMatch & result._1
    }
    if (!isMatch) {
      retDataSnpstVal = this.dataSnpstVal.::(ListDataSnpst(dataSnpstList))
    }
    if (this.dataSnpstVal.isEmpty) {
      retDataSnpstVal = List(ListDataSnpst(dataSnpstList))
    }
    retDataSnpstVal
  }


  def drop(dataSnpstList: List[DataSnpst]): List[ListDataSnpst] = {
    var retDataSnpstVal = List[ListDataSnpst]()
    var retDataSnpstValFinal = List[ListDataSnpst]()
    println("this.dataSnpstVal:" + dataSnpstVal)
    for (elem <- this.dataSnpstVal) {
      val result: List[DataSnpst] = elem.drop(dataSnpstList)
      println("DataSnpst.list.drop:" + result)
      retDataSnpstVal = retDataSnpstVal.::(ListDataSnpst(result))
    }
    //println("DROP retDataSnpstVal:" + retDataSnpstVal)
    //判断去掉为空值
    for (elem: ListDataSnpst <- retDataSnpstVal) {
      if (elem.listdataSnpstDic.nonEmpty) {
        retDataSnpstValFinal = retDataSnpstValFinal.::(elem)
      }
    }
    //println("DataSnpstVal: retDataSnpstVal [" + retDataSnpstValFinal + "]")
    retDataSnpstValFinal
  }

  def find(dataSnpstList: ListDataSnpst) = {

    //查询到的
    var foundFindVals = ListBuffer[DataSnpst]()
    //即将要查询的，也是未查询到的
    var findAndUnfoundedVals: mutable.Buffer[DataSnpst] = dataSnpstList.listdataSnpstDic.toBuffer
    println("Find Start : " + findAndUnfoundedVals)
    //初始化一个解析Map，用作将笛卡尔集回转为对象
    var reqPropTreeMap: TreeMap[String, List[Any]] = TreeMap[String, List[Any]]()
    for (elem: DataSnpst <- findAndUnfoundedVals) {
      reqPropTreeMap += (elem.columnName -> elem.columnValues)
    }

    for (elem: ListDataSnpst <- this.dataSnpstVal) {
      //findResult(找到的，未找到的)
      val findResult: (List[List[Any]], List[List[Any]]) = elem.find(findAndUnfoundedVals.toList)
      println("findResult._1: " + findResult._1)
      println("findResult._2: " + findResult._2)
      println("reqPropTreeMap: " + reqPropTreeMap)

      if (findResult._1.flatten.nonEmpty) {
        val founded: TreeMap[String, Any] = redistributeMap(findResult._1, reqPropTreeMap)
        for (elem <- founded) {
          foundFindVals += DataSnpst(elem._1, ckList(elem._2))
        }
      }

      val unfounded: TreeMap[String, Any] = redistributeMap(findResult._2, reqPropTreeMap)
      findAndUnfoundedVals.clear()
      println("Clear findAndUnfoundedVals:" + findAndUnfoundedVals)
      for (elem <- unfounded) {
        findAndUnfoundedVals += DataSnpst(elem._1, ckList(elem._2))
        //findAndUnfoundedVals = findAndUnfoundedVals - DataSnpst(elem._1,ckList(elem._2))
      }
    }
    println("FUNC IN foundFindVals:" + foundFindVals.toList)
    println("FUNC IN UnFoundFindVals:" + findAndUnfoundedVals.toList)
    (foundFindVals.toList, findAndUnfoundedVals.toList)
  }

}
