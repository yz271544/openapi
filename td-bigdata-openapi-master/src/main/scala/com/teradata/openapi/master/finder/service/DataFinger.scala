package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.RetDataFinger
import com.teradata.openapi.framework.util.{DicMapFunc, TimeFunc}
import com.teradata.openapi.master.finder.dao.ApiDataFingerDao

import scala.collection.mutable

/**
  * Created by John on 2016/4/22.
  * dataFingerMap:Map[data_finger:String  -> (file_loc:String, encode:String, hit_times:Int ,last_visit_time:Long)]
  */
class DataFinger(var dataFingerMap: mutable.Map[String, (String, String, Int, Long)]) extends DicMapFunc with TimeFunc with OpenApiLogging {

  override def toString = {
    var content = "dataFingerMap:["
    dataFingerMap.foreach(e => {
      val (k, v) = e
      content = content + " Key:" + k + " -> " + " Value:" + v + "\n"
    })
    content = content + "]"
    content
  }

  def reload() = {
    dataFingerMap = ApiDataFingerDao().loadDataFingerMap
  }
  /**
    * 探查无格式数据指纹，找不到返回None，找到返回Option[RetDataFinger]，并增加命中次数，更新最近访问时间戳
    *
    * @param dataFinger
    * @return
    */
  def find(dataFinger: String): Option[RetDataFinger] = {
    val ret: Option[(String, String, Int, Long)] = dataFingerMap.get(dataFinger)
    if (ret.isEmpty) None
    else {
      val file_loc: String = ret.get._1
      val encode: String = ret.get._2
      val hitTimes: Int = ret.get._3 + 1
      val lastVisitTimeStamp: Long = System.currentTimeMillis()
      dataFingerMap += (dataFinger ->(file_loc, encode, hitTimes, lastVisitTimeStamp))
      DataFinger.apiDataFingerDao.upsertDataFinger(dataFinger, 1, file_loc, encode, Option(hitTimes), Option(lastVisitTimeStamp))
      Some(RetDataFinger(dataFinger, file_loc, encode))
    }
  }

  /**
    * 清理删除数据缓存，从dataFingerMap中进行删除Key=data_finger，并更新表里的有效状态为无效0
    *
    * @param data_finger
    * @param eff_flag
    * @return
    */
  def drop(data_finger: String, eff_flag: Int = 0): Boolean = {
    try {
      dataFingerMap.-=(data_finger)
      //write drop finger to DB
      DataFinger.apiDataFingerDao.upsertDataFinger(data_finger, eff_flag)
    } catch {
      case e: Exception => return false
    }
    true
  }

  /**
    * 增加数据缓存，增加Key=data_finger到dataFingerMap中，并更新表里的有效状态为有效1
    *
    * @param data_finger
    * @param file_loc
    * @param eff_flag
    * @return
    */
  def add(data_finger: String, file_loc: String, encode: String, eff_flag: Int = 1): Boolean = {
    try {
      dataFingerMap += (data_finger ->(file_loc, encode, 0, System.currentTimeMillis()))
      //write add finger to DB
      DataFinger.apiDataFingerDao.upsertDataFinger(data_finger, eff_flag, file_loc)
    } catch {
      case e: Exception => return false
    }
    true
  }

  def getCleanDataFingerList(hitTimeMin: Int, visitTimeMax: Int, visitTimeUnit: Option[String]): List[RetDataFinger] = {
    var cleanDataFingerList: List[RetDataFinger] = List()
    for (elem <- dataFingerMap if elem._2._3 <= hitTimeMin && getBetweenTime(elem._2._4, System.currentTimeMillis(), visitTimeUnit.getOrElse("DAY")) >= visitTimeMax) {
      cleanDataFingerList = cleanDataFingerList :+ RetDataFinger(elem._1, elem._2._1, elem._2._2)
    }
    cleanDataFingerList
  }

  def cleanDataFingerCache(cleanDataFingerList: List[RetDataFinger]) = {
    for (elem <- cleanDataFingerList) {
      drop(elem.dataFinger)
    }
  }
}

object DataFinger {
  /*val dataFinger1 = "abcd1111"
  val dataFingerLocal1 = "/data/etl/dataCache/DATAFILE1"
  val dataFinger2 = "abcd2222"
  val dataFingerLocal2 = "/data/etl/dataCache/DATAFILE2"
  val dataFinger3 = "abcd3333"
  val dataFingerLocal3 = "/data/etl/dataCache/DATAFILE3"*/

  //val dataFingerMap = mutable.Map(dataFinger1 -> dataFingerLocal1, dataFinger2 -> dataFingerLocal2, dataFinger3 -> dataFingerLocal3)
  val apiDataFingerDao = ApiDataFingerDao()
  val dataFingerMap: mutable.Map[String, (String, String, Int, Long)] = apiDataFingerDao.loadDataFingerMap

  def apply() = new DataFinger(dataFingerMap)
}