package com.teradata.openapi.master.cacher.service

import com.codahale.jerkson._
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.CacheTable
import com.teradata.openapi.framework.model.{ApiCacheRulesRow, ApiDataSnpstCacheInfoRow, ApiTabCacheListRow, CacheRangeRegex}
import com.teradata.openapi.framework.util.{DicMapFunc, TimeFunc}
import com.teradata.openapi.master.cacher.dao.{ApiCacheRulesDao, ApiDataSnpstCacheInfoDao, ApiTabCacheListDao}

import scala.collection.immutable.TreeMap
import scala.util.matching.Regex

/**
  * Created by John on 2016/6/30.
  */
class DataSnpstCacheInfo() extends DicMapFunc with TimeFunc with OpenApiLogging {
  val apiDataSnpstCacheInfos: Seq[ApiDataSnpstCacheInfoRow] = ApiDataSnpstCacheInfoDao().loadAllCacheInfo()
  val apiTabCacheList: Seq[ApiTabCacheListRow] = ApiTabCacheListDao().loadAllCacheList()
  val apiCacheRules: Seq[ApiCacheRulesRow] = ApiCacheRulesDao().loadAllCacheRules()
  /**
    * dataSnpstCacheInfoMap:
    * Key   = DataSnpst(schemaName,tableName,data_snpst_name_val)
    * Value = Int ----->  0需要进缓存 1已Hold在缓存 2需清出缓存 3已清出缓存
    * = Int ----->  hitTimes : default 0
    * = Long ---->  Last_visit_time : default System.currentTimeMillis()
    */
  val dataSnpstCacheInfoMap: scala.collection.mutable.Map[DataSnpst, (Int, Int, Long)] = scala.collection.mutable.Map()
  val tabCacheListMap: scala.collection.mutable.Map[String, Int] = scala.collection.mutable.Map()

  if (apiDataSnpstCacheInfos.isEmpty) log.info("System initing,The CacheInfo is empty.")
  else {
    for (elem <- apiDataSnpstCacheInfos) {
      val dsnv: TreeMap[String, List[Any]] = try {
        mapToTreeMapVallst(Json.parse[Map[String, List[Any]]](elem.data_snpst_name_val))
      } catch {
        case e: ParsingException => throw e
        case e: Exception => throw e
      }
      val ds = new DataSnpst(elem.schema_name, elem.tab_name, dsnv)
      dataSnpstCacheInfoMap += (ds ->(elem.isnt_cache, elem.hit_times.getOrElse(0), elem.last_visit_time.getOrElse(System.currentTimeMillis())))
    }
  }

  if (apiTabCacheList.isEmpty) log.info("The CacheList is empty.")
  else {
    for (elem <- apiTabCacheList) {
      val tableFullName = elem.schema_name + "." + elem.tab_name
      val hitTimes: Int = elem.hit_times.getOrElse(0)
      tabCacheListMap += (tableFullName -> hitTimes)
    }
  }

  def showTabCacheListMap = {
    var content = ""
    tabCacheListMap.foreach(e => {
      val (k, v) = e
      content = content + " Key:" + k + " -> " + " Value:" + v + "\n"
    })
    content
  }

  def showDataSnpstCacheInfoMap = {
    var content = ""
    dataSnpstCacheInfoMap.foreach(e => {
      val (k, v) = e
      content = content + " Key:" + k + " -> " + " Value:" + v + "\n"
    })
    content
  }
  /**
    * 判断是否需要进入缓存，当表在CacheList中时代表应该表应该进入缓存，判断CacheInfo中是否存在该表的该快照是否在缓存中
    *
    * @param findTableFullName = schemaName.tableName
    * @param propMap           = {DEAL_DATE:List[201601,201602],REGION_CODE:List[01,02,03,04,05,06...]}
    * @return
    */
  def findInCache(findTableFullName: String, propMap: TreeMap[String, List[Any]]): Boolean = {
    log.debug("findInCache parameters:" + findTableFullName + " propMap:" + propMap)
    val schemaName = findTableFullName.split('.')(0)
    val tableName = findTableFullName.split('.')(1)
    val dsnv: String = Json.generate(propMap)
    log.debug("showTabCacheListMap:" + showTabCacheListMap)
    log.debug("showDataSnpstCacheInfoMap:" + showDataSnpstCacheInfoMap)
    var isShouldCache: Boolean = if (tabCacheListMap.getOrElse(findTableFullName, 0) >= 5) true else false
    log.debug("isShouldCache:" + isShouldCache)
    if (isShouldCache) {
      val findDataSnpstObj: DataSnpst = new DataSnpst(schemaName, tableName, propMap)
      log.debug("findDataSnpstObj:" + findDataSnpstObj + " schemaName:" + findDataSnpstObj.schemaName + " tableName:" + findDataSnpstObj.tableName + " propMap:" + findDataSnpstObj.data_snpst_name_val)
      val isNeedCache = dataSnpstCacheInfoMap.getOrElse(findDataSnpstObj, (-1, -1, -1L))._1
      log.debug("一:" + dataSnpstCacheInfoMap.getOrElse(findDataSnpstObj, (-1, -1, -1L))._1 + " 二:" + dataSnpstCacheInfoMap.getOrElse(findDataSnpstObj, (-1, -1, -1L))._2 + " 三:" + dataSnpstCacheInfoMap.getOrElse(findDataSnpstObj, (-1, -1, -1L))._3)
      log.debug("isNeedCache:" + isNeedCache)
      //应该在缓存，但是当前并不在，所以增加一个需要进缓存的任务 -> isEmpty不纳入缓存，3已清出缓存
      if ((isNeedCache == 0) || (isNeedCache == -1) || (isNeedCache == 3)) {
        dataSnpstCacheInfoMap += (findDataSnpstObj ->(0, 0, System.currentTimeMillis()))
        initCacheRcd(schemaName, tableName, dsnv, Option(0), Option(0), Option(System.currentTimeMillis()))
      }
      //应该在缓存，且当前已经在缓存的，则需要对命中次数加一，并更新最后一次访问时间
      else {
        if (isNeedCache == 1) {
          //val isCacheFlag = dataSnpstCacheInfoMap.get(findDataSnpstObj).get._1
          val hitTimes = dataSnpstCacheInfoMap.get(findDataSnpstObj).get._2 + 1
          dataSnpstCacheInfoMap += (findDataSnpstObj ->(isNeedCache, hitTimes, System.currentTimeMillis()))
          incrCacheHitTimes(schemaName, tableName, dsnv, isNeedCache, Option(hitTimes), Option(System.currentTimeMillis())) //已经在缓存中Hold住才能够进行追加命中次数和访问时间更新
        }
        isShouldCache = false
      }
    }
    isShouldCache
  }

  /**
    * 不在日志扫描结果中的表，进行清出缓存
    *
    * @return
    */
  def findOutCacheByList(): List[String] = {
    var outCacheList: List[String] = List()
    for (elem <- dataSnpstCacheInfoMap if elem._2._1 == 1) {
      val tableFullName = elem._1.schemaName + "." + elem._1.tableName
      if (tabCacheListMap.get(tableFullName).isEmpty) {
        outCacheList = outCacheList :+ tableFullName
        ApiDataSnpstCacheInfoDao().updateAllMatchCacheStat(elem._1.schemaName, elem._1.tableName, 2)
      }
    }
    outCacheList
  }

  /**
    * 需定时调度：按照清理策略匹配查找哪些表的快照需要清出缓存
    * @return
    */
  def findOutCacheByStrategy(): List[CacheTable] = {
    var outCacheList: List[CacheTable] = List()
    for (rule_elem <- apiCacheRules if rule_elem.rule_type == 1) {
      val rangeRegexList: List[String] = Json.parse[CacheRangeRegex](rule_elem.rule_range).tbFullNameRegex
      for (rangeRegex <- rangeRegexList) {
        for (elem <- dataSnpstCacheInfoMap if matchRegex(rangeRegex, elem._1.schemaName + "_" + elem._1.tableName) && ((elem._2._2 <= rule_elem.hit_times_min.getOrElse(5)) && getBetweenTime(elem._2._3, System.currentTimeMillis(), rule_elem.visit_time_unit.getOrElse("DAY")) >= rule_elem.visit_time_max.getOrElse(5))) {
          val outCachetable: CacheTable = new CacheTable(elem._1.schemaName, elem._1.tableName, elem._1.data_snpst_name_val, 0, "DROP")
          outCacheList = outCacheList :+ outCachetable
        }
      }
    }
    outCacheList
  }


  def matchRegex(elemRegex:String,tableName:String): Boolean ={
    val rangeRegex: Regex = s"""($elemRegex)""".r
    val matchTbName: String = tableName match {
      case rangeRegex(tbName) => tbName
      case _ => "NONE"
    }
    val matchFlag: Boolean = if (matchTbName equals "NONE") false else true
    matchFlag
  }

  /**
    * 增加一条需要缓存的记录
    *
    * @param schemaName
    * @param tableName
    * @param dsnv
    * @return
    */
  def initCacheRcd(schemaName: String, tableName: String, dsnv: String, isnt_cache: Option[Int] = None, hit_times: Option[Int] = None, last_visit_time: Option[Long] = None): Int = {
    val retRows = ApiDataSnpstCacheInfoDao().upsert(schemaName, tableName, dsnv, isnt_cache.getOrElse(0), Option(hit_times.getOrElse(0)), Option(last_visit_time.getOrElse(System.currentTimeMillis())))
    retRows
  }

  /**
    * 更新缓存命中次数，最后一次访问时间
    *
    * @param schemaName
    * @param tableName
    * @param dsnv
    * @param isnt_cache
    * @return
    */
  def incrCacheHitTimes(schemaName: String, tableName: String, dsnv: String, isnt_cache: Int, hit_times: Option[Int] = None, last_visit_time: Option[Long] = None) = {
    val retRows = ApiDataSnpstCacheInfoDao().updHitTimes(schemaName, tableName, dsnv, isnt_cache, Option(hit_times.getOrElse(0)), Option(last_visit_time.getOrElse(System.currentTimeMillis())))
    retRows
  }

  /**
    * 数据已经缓存，重置缓存状态为1
    *
    * @param schemaName
    * @param tableName
    * @param propMap
    * @return
    */
  def updateCacheToHold(schemaName: String, tableName: String, propMap: TreeMap[String, List[Any]]) = {
    val updDataSnpstObj = new DataSnpst(schemaName, tableName, propMap)
    val (isCache, hitTimes, last_visit_time) = dataSnpstCacheInfoMap.getOrElse(updDataSnpstObj, (1, 0, System.currentTimeMillis()))
    dataSnpstCacheInfoMap += (updDataSnpstObj ->(1, hitTimes, last_visit_time))
    val updDataSnpstCacheInfoMap = ApiDataSnpstCacheInfoDao().upsert(schemaName, tableName, Json.generate(propMap), 1)
    updDataSnpstCacheInfoMap
  }

  /**
    * 数据需要清出缓存，设置状态为2
    *
    * @param schemaName
    * @param tableName
    * @param propMap
    * @return
    */
  def updateCacheNeedOut(schemaName: String, tableName: String, propMap: TreeMap[String, List[Any]]) = {
    val updDataSnpstObj = new DataSnpst(schemaName, tableName, propMap)
    val (isCache, hitTimes, last_visit_time) = dataSnpstCacheInfoMap.getOrElse(updDataSnpstObj, (2, 0, System.currentTimeMillis()))
    dataSnpstCacheInfoMap += (updDataSnpstObj ->(2, hitTimes, last_visit_time))
    val updDataSnpstCacheInfoMap: Int = ApiDataSnpstCacheInfoDao().upsert(schemaName, tableName, Json.generate(propMap), 2)
    updDataSnpstCacheInfoMap
  }

  /**
    * 数据已经清出缓存，设置状态为3
    *
    * @param schemaName
    * @param tableName
    * @param propMap
    * @return
    */
  def updateCacheHaveOuted(schemaName: String, tableName: String, propMap: TreeMap[String, List[Any]]) = {
    val updDataSnpstObj = new DataSnpst(schemaName, tableName, propMap)
    //dataSnpstCacheInfoMap += (updDataSnpstObj -> 3)
    dataSnpstCacheInfoMap - updDataSnpstObj
    val updDataSnpstCacheInfoMap = ApiDataSnpstCacheInfoDao().upsert(schemaName, tableName, Json.generate(propMap), 3)
    updDataSnpstCacheInfoMap
  }

  def refreshCacheRcd() = {
    val refreshApiDataSnpstCacheInfos: Seq[ApiDataSnpstCacheInfoRow] = ApiDataSnpstCacheInfoDao().loadAllCacheInfo()
    dataSnpstCacheInfoMap.clear()
    if (refreshApiDataSnpstCacheInfos.isEmpty) log.info("System initing,The CacheInfo is empty.")
    else {
      for (elem <- refreshApiDataSnpstCacheInfos) {
        val dsnv: TreeMap[String, List[Any]] = try {
          mapToTreeMapVallst(Json.parse[Map[String, List[Any]]](elem.data_snpst_name_val))
        } catch {
          case e: ParsingException => throw e
          case e: Exception => throw e
        }
        val ds = new DataSnpst(elem.schema_name, elem.tab_name, dsnv)
        dataSnpstCacheInfoMap += (ds ->(elem.isnt_cache, elem.hit_times.getOrElse(0), elem.last_visit_time.getOrElse(System.currentTimeMillis())))
      }
    }
  }

  def refreshTabCacheList() = {
    val refreshApiTabCacheList: Seq[ApiTabCacheListRow] = ApiTabCacheListDao().loadAllCacheList()
    if (refreshApiTabCacheList.isEmpty) log.info("The CacheList is empty.")
    else {
      tabCacheListMap.clear()
      for (elem <- refreshApiTabCacheList) {
        val tableFullName = elem.schema_name + "." + elem.tab_name
        val hitTimes: Int = elem.hit_times.getOrElse(0)
        tabCacheListMap += (tableFullName -> hitTimes)
      }
    }
  }

}

object DataSnpstCacheInfo {

  def apply() = {
    new DataSnpstCacheInfo()
  }
}

object DataSnpstCacheInfoTest extends DicMapFunc {
  def main(args: Array[String]) {
    //ApiDataSnpstCacheInfoDao().updateAllMatchCacheStat("PMID3","TB_MID_PAR_TOT_USER_MON",1)
    val testStr =
      """{"DEAL_DATE":[201601,201602]}"""
    val p1: TreeMap[String, List[Any]] = mapToTreeMapVallst(Json.parse[Map[String, List[Any]]](testStr))
    println(p1)
    println(p1.getClass)

    val jsonStr = Json.generate(p1)
    println("Json:" + jsonStr)
    println(jsonStr.getClass)
  }
}