package com.teradata.openapi.master.test

import com.teradata.openapi.framework.util.DicMapFunc

import scala.collection.GenSeq
import scala.collection.immutable.TreeMap

/**
  * Created by John on 2016/8/1.
  */
object TestAggr extends DicMapFunc{
  def main(args: Array[String]) {

    var retMap = TreeMap[String, List[Any]]()
    val sourceList = List(
      List(201601, "00"), List(201601, "01"), List(201601, "02"), List(201601, "03"),
      List(201602, "00"), List(201602, "01"), List(201602, "02"), List(201602, "03"),
      List(201603, "00"), List(201603, "01"), List(201603, "02")
    )



    //sourceList.map(_(0)).distinct

    /*val sourceList = List(
      List(201601), List(201601), List(201601), List(201601),
      List(201602), List(201602), List(201602), List(201602),
      List(201603), List(201603), List(201603)
    )*/

    val columnArg = TreeMap("deal_date" -> List(201601, 201602, 201603), "region_code" -> List("00", "01", "02", "03"))
    //val columnArg = TreeMap("deal_date" -> List(201601, 201602, 201603))
    val columnKeyName: List[String] = columnArg.keySet.toList.sortWith(_ < _)
    println("columnKeyName:" + columnKeyName)
    val dataSnpstColumnLen =  columnKeyName.length

    if (dataSnpstColumnLen == 1){
      val res: List[Any] = sourceList.flatten(ckList).distinct
      for (elemColumnName <- columnKeyName) {
        retMap += (elemColumnName -> res)
      }
      println("res:" + res)
    } else if (dataSnpstColumnLen == 2) {
      val sorcGroupMap: Map[Any, List[Any]] = sourceList.groupBy(_.head.asInstanceOf[Any]).mapValues(_.map(_.tail.head.asInstanceOf[Any]))
      val sorcGroupMap2: Map[Any, List[Any]] = mapTolist(sorcGroupMap).groupBy(_.tail.head.asInstanceOf[Any]).mapValues(_.map(_.head.asInstanceOf[Any]))
      val newRe2: Map[List[Any], Any] =  for((k,v) <- sorcGroupMap2) yield (v,k)
      println("newRe2:" + newRe2)
    }




   /*val groupMap: Map[Any, List[Any]] = sourceList.groupBy(_.head.asInstanceOf[Any]).mapValues(_.map(_.tail.head.asInstanceOf[Any]))
    println("groupMap:" + groupMap)
    //println("aggregate:")
    //groupMap.aggregate()
    val groupMapValList: List[List[Any]] = mapTolist(groupMap)
    println("groupMapValList:" + groupMapValList)
    val re2: Map[Any, List[Any]] = groupMapValList.groupBy(_.tail.head).mapValues(_.map(_.head))
    println("groupMapBy2 ::::: " + re2)
    val newRe2: Map[List[Any], Any] =  for((k,v) <- re2) yield (v,k)
    println("newRe2:" + newRe2)*/




   /* val map2List =  mapTolistGenel(newRe2)
      println(map2List)
    println("map2List.transpose:" + map2List.transpose)

    println("transpose:")
    val transpoList: List[List[Any]] = sourceList.transpose.map(_.distinct)
    println(transpoList)
*/
    /*val aggrList = sourceList.aggregate()
    println("aggrList:" + aggrList)*/

    println(retMap)
  }

  def aggr(sourceList: List[List[Any]],level:Int) = {
    var tempList: List[List[Any]] = sourceList
    for(i <- 1 to level){
      val tempLista: Map[Any, List[Any]] =
      if (i % 2 == 1){
        tempList.groupBy(_.head).mapValues(_.map(_.tail.head))
      } else {
        tempList.groupBy(_.tail.head).mapValues(_.map(_.head))
      }


    }
  }

}
