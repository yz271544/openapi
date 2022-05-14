package com.teradata.openapi.framework.util

import java.text.SimpleDateFormat
import java.util.Date

import com.xiaoleilu.hutool.util.DateUtil._
import it.sauronsoftware.cron4j.{InvalidPatternException, SchedulingPattern}


/**
  * Created by John on 2016/7/6.
  */
trait TimeFunc {

  def getCurrentTimeMillis: Long = System.currentTimeMillis()

  def timeMillsToDate(timeMills: Long, format: String = "yyyy-MM-dd"): String = {
    val sdf:SimpleDateFormat = try {
      new SimpleDateFormat(format)
    } catch {
      case e : IllegalArgumentException => throw e.fillInStackTrace()
      case e : NullPointerException => throw e.fillInStackTrace()
    }
    val date:String = sdf.format(new Date(timeMills))
    date
  }

  def timeMillsToTimeStamp(timeMills: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String = {
    val sdf:SimpleDateFormat = try {
      new SimpleDateFormat(format)
    } catch {
      case e : IllegalArgumentException => throw e.fillInStackTrace()
      case e : NullPointerException => throw e.fillInStackTrace()
    }
    val date:String = sdf.format(new Date(timeMills))
    date
  }

  def toDate(timeMills:Long): Date ={
    val date = new Date(timeMills)
    date
  }

  /**
    * 计算两个时间戳相差的时间
 *
    * @param start_Time
    * @param end_Time
    * @param diffType
    * @return
    */
  def getBetweenTime(start_Time:Long,end_Time:Long,diffType:String): Long ={
    val (diffField,toFormat) = diffType.toUpperCase match {
      case "DAY" => (DAY_MS,NORM_DATE_PATTERN) // 86400L * 1000
      case "HOUR" => (HOUR_MS,NORM_DATETIME_PATTERN) // 3600L * 1000
      case "WEEK" => (DAY_MS * 7,NORM_DATE_PATTERN)
      case "MIN" => (MINUTE_MS,NORM_DATETIME_MINUTE_PATTERN) // 60L * 1000
      case "SEC" => (SECOND_MS,NORM_DATETIME_PATTERN)
      case _ => (1L,NORM_DATETIME_MS_PATTERN)
    }
    val diffValue: Long = diff(parse(format(toDate(start_Time),toFormat)),parse(format(toDate(end_Time),toFormat)),diffField)
    math.abs(diffValue)
  }

  def cronTriggerMatch(cronExpression:String,timeMills :Long) = {
    var isMatch = false
    try {
      val sch = new SchedulingPattern(cronExpression)
      isMatch = sch.`match`(timeMills)
    } catch {
      case e: InvalidPatternException => throw e
      case e: Exception => throw e
    }
    isMatch
  }
}


object TimeFuncChk extends TimeFunc{
  def main(args: Array[String]) {
    println(toDate(System.currentTimeMillis()))
    println(getBetweenTime(1467713914611L,1467792088616L,"DAY"))
    println(timeMillsToTimeStamp(1467713914611L))
    println(timeMillsToTimeStamp(getCurrentTimeMillis))
  }
}