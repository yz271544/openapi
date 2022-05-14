package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.util.DicMapFunc

/**
  * Created by John on 2016/4/11.
  */
case class ConditionSnapshot() extends DicMapFunc{

  type ListAny = List[Any]
  var cMap: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map()
  def add (columnName: String, columnValue: Any){
    cMap += (columnName -> columnValue)
  }

}

object TestConditionSnapshot {
  def main(args: Array[String]) {
    val tcs = ConditionSnapshot()
    tcs.add("deal_date", Set(201601, 201602, 201603))
    tcs.add("region_code", Set("01", "02", "03"))
    tcs.add("region_code", Set("01", "02", "03"))
    println(tcs.cMap)
    //val tcs = new ConditionSnapshot("deal_date", Set(201601, 201602, 201603))


    /*tSet.foreach(e => {
      /*val obj  = e.asInstanceOf[ConditionSnapshot]
      println(obj.columnName)
      println(obj.columnValue)
      val tMap: scala.collection.mutable.Map[String, Set[Any]] = obj.conditionMap(obj.columnName,obj.columnValue)
      println(tMap)*/
      println(e)
    })*/
  }
}

