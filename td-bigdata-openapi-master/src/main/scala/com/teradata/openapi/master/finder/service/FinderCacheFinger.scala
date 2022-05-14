package com.teradata.openapi.master.finder.service

import java.io.Serializable
import java.lang.{Boolean, Long}
import java.util

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.model.FinderCacheFingerRow
import com.teradata.openapi.framework.util.RedisUtil
import com.teradata.openapi.master.finder.dao.{FinderCacheFingerDao, ReqInfoDao, ReqInfoHisDao}

import scala.collection.immutable.TreeMap
import scala.collection.{AbstractSeq, Set, mutable}
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
/**
  * Created by John on 2016/10/25.
  */
class FinderCacheFinger extends OpenApiLogging{
  val finderCacheFingerDao = FinderCacheFingerDao()
  val reqInfoHisDao = ReqInfoHisDao()
  val reqInfoDao = ReqInfoDao()
  /**
    * 获取指纹信息和指纹所对应的请求ID清单
    * @param schemaName
    * @param tableName
    * @param jsonCondtionMap
    * @return fingerBuffers:  mutable.Map(String, ListBuffer(String))
    */
  def findFingerMap(schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]]): mutable.Map[String, ListBuffer[String]] = {
    val fingerBuffers = mutable.Map[String,ListBuffer[String]]()
    for (elem <- jsonCondtionMap) {
      val finderCacheFingers: Seq[FinderCacheFingerRow] = finderCacheFingerDao.loadByCycleInfo(schemaName,tableName,elem._1,elem._2.map(_.toString))
      for (elem <- finderCacheFingers) {
        fingerBuffers  += (elem.form_finger -> fingerBuffers.getOrElse(elem.form_finger,ListBuffer()).+=(elem.req_id))
      }
    }
    fingerBuffers
  }

  def cleanRedis(fingerBuffers: mutable.Map[String,ListBuffer[String]]): List[String] = {
    val redisKeys: mutable.Set[String] = asScalaSet(RedisUtil.getInstance.getAllkeys("*"))
    val intersectFingers: Set[String] = fingerBuffers.keySet & redisKeys
    val successList = ListBuffer[String]()
    val failList = ListBuffer[String]()
    for (elem <- intersectFingers) {
      try {
        val delRet: Long = RedisUtil.getInstance.delKey(elem)
        if (delRet >= 0) {
          successList ++ fingerBuffers.get(elem)
        } else {
          failList ++ fingerBuffers.get(elem)
        }
      } catch {
        case e: Exception => {
          failList += elem
          log.info("The Redis status exception:" + e.getMessage)
        }
      }
    }
    val successLists = successList.toList
    val ifi: Int = reqInfoHisDao.insertFromInfo(this.getClass.getSimpleName,successLists)
    if (ifi > 0) reqInfoDao.deleteRange(successLists)
    successLists
  }
}

object FinderCacheFinger {
  def apply(): FinderCacheFinger = new FinderCacheFinger()
}


object FinderCacheFingerTest {
  def main(args: Array[String]) {
    val f = FinderCacheFinger()
    val ffm = f.findFingerMap("RPTMART3","TB_RPT_BO_MON",TreeMap("DEAL_DATE" -> List("201604","201605")))
    for (elem <- ffm) {
      println(elem)
    }
  }
}