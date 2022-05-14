package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.RetFormatFinger
import com.teradata.openapi.framework.util.{DicMapFunc, TimeFunc}
import com.teradata.openapi.master.finder.dao.ApiFormatFingerDao

import scala.collection.mutable

/**
  * Created by John on 2016/4/22.
  * formatFingerMap:mutable.Map[form_finger:String -> (file_loc:String, data_finger:String, hit_times:Int, last_visit_time:Long)]
  */
class FormatFinger(var formatFingerMap: mutable.Map[String, (String, String, Int, Long)]) extends DicMapFunc with TimeFunc with OpenApiLogging {

  override def toString = {
    var content = "formatFingerMap:["
    formatFingerMap.foreach(e => {
      val (k, v) = e
      content = content + " Key:" + k + " -> " + " Value:" + v + "\n"
    })
    content = content + "]"
    content
  }

  def reload() = {
    formatFingerMap = ApiFormatFingerDao().loadFormFingerMap
  }

  /**
    * 探查有格式数据指纹，找不到返回None，找到返回Option[RetFormatFinger]，并增加命中次数，更新最近访问时间戳
    *
    * @param formatFinger
    * @return
    */
  def find(formatFinger: String): Option[RetFormatFinger] = {
    val ret = formatFingerMap.get(formatFinger)
    if (ret.isEmpty) None
    else {
      val file_loc = ret.get._1
      val dataFinger = ret.get._2
      val hitTimes: Int = ret.get._3 + 1
      val lastVisitTimeStamp: Long = System.currentTimeMillis()
      formatFingerMap += (formatFinger ->(file_loc, dataFinger, hitTimes, lastVisitTimeStamp))
      FormatFinger.apiFormatFingerDao.upsertFormFinger(formatFinger, 1, file_loc, dataFinger, Option(hitTimes), Option(lastVisitTimeStamp))
      Some(RetFormatFinger(formatFinger, file_loc))
    }
  }

  /**
    * 清理删除数据缓存，从formatFingerMap中进行删除Key=form_finger，并更新表里的有效状态为无效0
    *
    * @param form_finger
    * @param eff_flag
    * @return
    */
  def drop(form_finger: String, eff_flag: Int = 0): Boolean = {
    try {
      formatFingerMap.-=(form_finger)
      //write drop finger to DB
      FormatFinger.apiFormatFingerDao.upsertFormFinger(form_finger, eff_flag)
    } catch {
      case e: Exception => return false
    }
    true
  }

  /**
    * 增加数据缓存，增加Key=form_finger到formatFingerMap中，并更新表里的有效状态为有效1
    *
    * @param data_finger
    * @param form_finger
    * @param file_loc
    * @param eff_flag
    * @return
    */
  def add(form_finger: String, file_loc: String, data_finger: String, eff_flag: Int = 1): Boolean = {
    try {
      formatFingerMap += (form_finger ->(file_loc, data_finger, 0, System.currentTimeMillis()))
      //write add finger to DB
      FormatFinger.apiFormatFingerDao.upsertFormFinger(form_finger, eff_flag, file_loc, data_finger)
    } catch {
      case e: Exception => return false
    }
    true
  }

  def getCleanFormatFingerList(hitTimeMin: Int, visitTimeMax: Int, visitTimeUnit: Option[String]): List[RetFormatFinger] = {
    var cleanFormatFingerList: List[RetFormatFinger] = List()
    for (elem <- formatFingerMap if elem._2._3 <= hitTimeMin && getBetweenTime(elem._2._4, System.currentTimeMillis(), visitTimeUnit.getOrElse("DAY")) >= visitTimeMax) {
      cleanFormatFingerList = cleanFormatFingerList :+ RetFormatFinger(elem._1,elem._2._2)
    }
    cleanFormatFingerList
  }

  def cleanFormatFingerCache(cleanFormatFingerList: List[RetFormatFinger]) = {
    for (elem <- cleanFormatFingerList) {
      drop(elem.formatFinger)
    }
  }

}

object FormatFinger {
  /*val formatFinger1 = "abcd1111JSON"
  val formatFingerLocal1 = "/data/etl/formatCache/FORMATFILE1"
  val formatFinger2 = "abcd2222XML"
  val formatFingerLocal2 = "/data/etl/formatCache/FORMATFILE2"
  val formatFinger3 = "abcd3333TXT"
  val formatFingerLocal3 = "/data/etl/formatCache/FORMATFILE3"*/
  //val formatFingerMap = mutable.Map(formatFinger1 -> formatFingerLocal1, formatFinger2 -> formatFingerLocal2, formatFinger3 -> formatFingerLocal3)
  val apiFormatFingerDao = ApiFormatFingerDao()
  val formatFingerMap: mutable.Map[String, (String, String, Int, Long)] = apiFormatFingerDao.loadFormFingerMap

  def apply() = new FormatFinger(formatFingerMap)
}

object FormatFingerTest {
  def main(args: Array[String]) {
    val a: Option[RetFormatFinger] = FormatFinger().find("abd")
    println(a)
  }
}