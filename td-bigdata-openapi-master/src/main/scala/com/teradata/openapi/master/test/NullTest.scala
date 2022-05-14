package com.teradata.openapi.master.test

import com.teradata.openapi.framework.model.{DataSnpst, ListDataSnpst}

/**
  * Created by John on 2016/10/18.
  */
object NullTest {
  def main(args: Array[String]) {
    //false
    val aa: List[ListDataSnpst] = List(ListDataSnpst(List(DataSnpst(columnName = "Deal_date",columnValues = List(201604,201605)))))
    //true
    val bb: List[ListDataSnpst] = List(ListDataSnpst(List()))
    println(listIsNullFlag(aa))
  }

  def isNullFlag(findRet: List[DataSnpst]): Boolean = {
    val isNullFlag = findRet.map(_.columnValues.isEmpty).aggregate(true)(_ & _, _ & _)
    isNullFlag
  }

  def listIsNullFlag(findRet: List[ListDataSnpst]): Boolean = {
    var isNull :Boolean = true
    for (elem: ListDataSnpst <- findRet) {
      isNull = isNull && isNullFlag(elem.listdataSnpstDic)
    }
    isNull
  }
}
