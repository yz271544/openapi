package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{RetLocation, TableInfo}

import scala.collection.immutable.TreeMap
import scala.collection.{Map, Set, mutable}
import scala.reflect.ClassTag
import scala.util.control.Breaks._
import com.codahale.jerkson.Json._
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.cacher.service.DataSnpstCacheInfo
import com.teradata.openapi.master.finder.dao.ApiDataSnpstDdDao
import com.teradata.openapi.master.resolver.dao.SourceInfoDao

/**
  * Created by John on 2016/4/20.
  */
class DataSnapshotDic(sourceids: List[Int], sourceSnapshotDic: scala.collection.mutable.Map[String, String])
  extends SnapshotDic with DicMapFunc with OpenApiLogging {
  val cacheDic = DataSnpstCacheInfo()
  val keys = sourceSnapshotDic.keySet
  val dataSnapshotMap: scala.collection.mutable.Map[String, scala.collection.mutable.Map[String, Any]] = scala.collection.mutable.Map()
  if (sourceSnapshotDic.isEmpty) println("Sorry! This OpenAPI system haven't loaded data to service.")
  else {
    for (key <- keys) {
      val b: String = sourceSnapshotDic.getOrElse(key, "")
      //println("[b]:"+ b)
      var jsonMap: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map()
      jsonMap = jsonToMapLML(b)
      //println("jsonMap:" + jsonMap)
      dataSnapshotMap.+=(key -> jsonMap)
    }
    /*//查看快照字典Map的信息
    println("查看快照字典Map的信息:")
    log.debug("查看快照字典Map的信息:")
    dataSnapshotMap.foreach(e => {
      val (k, v) = e
      println("识别库里的信息：" + k + " -> " + v)
      log.debug(k + " -> " + v)
    })*/
  }

  override def toString = {
    //查看快照字典Map的信息
    var content = ""
    dataSnapshotMap.foreach(e => {
      val (k, v) = e
      content = content + " Key:" + k + " -> " + " Value:" + v + "\n"
    })
    content
  }

  /*val mm: Map[String, mutable.Map[String, Any]] = dataSnapshotMap.filterKeys(_.endsWith("_0"))
  println("MMMMMMMMMM:" + mm.isEmpty)*/
  /**
    *
    * @param sourceId
    * @param tableFullName
    * @param jsonCondtionMap
    * @return 返回该表的快照被增加到DIC中后,相关API的list
    */
  def add(sourceId: Int, tableFullName: String, jsonCondtionMap: TreeMap[String, List[Any]]): List[Int] = {
    log.debug("DataSnapshotDic add: sourceId: " + sourceId + " tableFullName:" + tableFullName + " jsonCondtionMap:" + jsonCondtionMap)
    println("DataSnapshotDic add: sourceId: " + sourceId + " tableFullName:" + tableFullName + " jsonCondtionMap:" + jsonCondtionMap)
    val upperCaseTableFullName = tableFullName.toUpperCase
    val updApiIdList = List[Int]()
    //格式校验
    //val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(condtionMap))
    //更新字典
    for (elemMap <- dataSnapshotMap) {
      for (elemTableDataSnpstMap <- elemMap._2) {
        val dicTableName = elemTableDataSnpstMap._1
        val dicTableValueMap: Any = elemTableDataSnpstMap._2
        if (dicTableName equals upperCaseTableFullName) {
          var snpstMap  =  TreeMap[String,List[Any]]()
          var snpstList: List[TreeMap[String, List[Any]]] = List[TreeMap[String, List[Any]]]()
          for (fieldElem <- ckList(dicTableValueMap)) {
            var fieldTreeMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(ckMap(fieldElem))
            if (elemMap._1.endsWith("_" + sourceId) && (jsonCondtionMap.keySet &~ fieldTreeMap.keySet).isEmpty && (fieldTreeMap.keySet &~ jsonCondtionMap.keySet).isEmpty) {
              var updList: List[TreeMap[String, List[Any]]] = List[TreeMap[String, List[Any]]]()
              for (elem <- fieldTreeMap) {
                val intersectVal: List[Any] = (jsonCondtionMap.getOrElse(elem._1,List()) union elem._2).distinct
                println("intersectVal:" + elem._1 + " -> " +intersectVal)
                fieldTreeMap += (elem._1 -> intersectVal)
                updList = updList.+:(fieldTreeMap)
              }
              snpstMap += (dicTableName -> updList)
              snpstList = snpstList.+:(snpstMap)
              //updList = updList.+:(jsonCondtionMap)
              println("jsonCondtionMap.keySet 相同:" + " apiId:" + elemMap._1.split('_')(0) + " apiVerion:" + elemMap._1.split('_')(1) + " sourceId:"+ elemMap._1.split('_')(2) + " data_snpst_val:" + generate(snpstList))

            }
            else {
              var insList: List[TreeMap[String, List[Any]]] = List[TreeMap[String, List[Any]]]()
              insList = insList.+:(jsonCondtionMap)

              //updList = updList.+:(fieldTreeMap)
              println("jsonCondtionMap.keySet 不同:" + " apiId:" + elemMap._1.split('_')(0) + " apiVerion:" + elemMap._1.split('_')(1) + " sourceId:"+ sourceId + " data_snpst_val:" + generate(insList))

            }
            //tableValueMap += (dicTableName -> updList)
          }
        }
        else {
          //tableValueMap += (elem._1 -> dicTableValueMap)
        }
      }

    }

    updApiIdList
  }

  def drop(sourceId: Int, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]]) = {
    //格式校验
    //val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(condtionMap))
    //更新字典
    for (elem <- dataSnapshotMap) {
      if (elem._1.split("_")(1).toInt.equals(sourceId)) {
        val apiId = elem._1.split("_")(0).toInt
        val apiVersion = elem._1.split("_")(1).toInt
        val sourceId = elem._1.split("_")(2).toInt
        val dicSourceID = elem._1
        var tableValueMap = mutable.Map[String, Any]()
        for (elem <- elem._2) {
          var dicTableName = elem._1
          val dicTableValueMap = elem._2
          if (elem._1 equals tableName) {
            dicTableName = elem._1
            var updList: List[TreeMap[String, List[Any]]] = List[TreeMap[String, List[Any]]]()
            for (elem <- ckList(elem._2)) {
              var dicTreeMap = mapToTreeMapVallst(ckMap(elem))
              if ((jsonCondtionMap.keySet &~ dicTreeMap.keySet).isEmpty && (dicTreeMap.keySet &~ jsonCondtionMap.keySet).isEmpty) {
                dicTreeMap.foreach(e => {
                  val (k, v) = e
                  val diffVals = v diff jsonCondtionMap(k)
                  if (diffVals.nonEmpty) dicTreeMap += (k -> diffVals)
                })
                if (dicTreeMap.nonEmpty) updList = updList.drop(updList.indexOf(dicTreeMap))
              }
              else {
                updList = updList.+:(dicTreeMap)
              }
              tableValueMap += (dicTableName -> updList)
            }
          }
          else {
            tableValueMap += (elem._1 -> dicTableValueMap)
          }
        }
        //dataSnapshotMap += (dicSourceID -> tableValueMap)
        val gdataSnpstVal = generate(tableValueMap)
        log.debug("[drop] ApiId = " + apiId + " apiVersion = " + apiVersion + " SourceId = " + sourceId + " snapshot_json = " + gdataSnpstVal)
        println("[drop] ApiId = " + apiId + " apiVersion = " + apiVersion + " SourceId = " + sourceId + " snapshot_json = " + gdataSnpstVal)
        if ("{}" equals gdataSnpstVal) {
          dataSnapshotMap -= dicSourceID //drop Mem
          DataSnapshotDic.apiDataSnpstDdDao.delSnpst(apiId, apiVersion, sourceId) //write drop to DB
        } else {
          dataSnapshotMap += (dicSourceID -> tableValueMap) //drop Mem
          DataSnapshotDic.apiDataSnpstDdDao.upsertSnpst(apiId, apiVersion, sourceId, gdataSnpstVal) //write drop to DB
        }
      }
    }
    /*println(":::::::::::::::查看快照字典Map DELETE后信息:::::::::::::::")
    dataSnapshotMap.foreach(e => {
      val (k, v) = e
      println(k + " -> " + v)
    })*/
  }

  def find(getFinderApiId: Int, getFinderApiVersion: Int, reqArgs: List[scala.collection.mutable.Map[String, Any]]): Option[RetLocation] = {
    val snapshotDicMap = dataSnapshotMap.filterKeys(m => (m.split("_")(0).toInt == getFinderApiId) && m.split("_")(1).toInt == getFinderApiVersion) //APINAME -> 获取字典位置Map，从字典里获取该APINAME所对应的APINAME_SOURCEID组合所对应的快照信息
    var finderList = List[TableInfo]()
    var finderAllFlag = true //是否探查完善
    var hitSet = scala.collection.immutable.Set[String]()
    reqArgs.foreach(e => {
      var finderFlag = false
      breakable {
        e.foreach(e => {
          //遍历“请求”待查集合 println("请求：" + finderDiffrecMap)
          val (reqTablename, reqPropArgs: Any) = e //在“该字典”中查找对应表的信息
          //判断识别reqPropArgs的Any类型为TreeMap[String, List[Any]]类型
          //println("reqTablename:"+reqTablename + " reqPropArgs:"+ reqPropArgs)
          val reqPropTreeMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(ckMap(reqPropArgs))
          //println("reqPropTreeMap:"+reqPropTreeMap)
          var fdlist: List[List[Any]] = cartesianProductStr(reqPropTreeMap.keySet.map(reqPropTreeMap).toList)
          //println("fdlist:"+fdlist)
          var snapshotDicKey = ""
          sourceids.foreach(e => {
            //遍历数据源列表 //取API+数据源
            val sourceId = e
            //endregion
            breakable {
              //取API+数据源Map //println("["+snapshotDicKey+"]")
              snapshotDicKey = getFinderApiId.toString.trim + "_" + getFinderApiVersion.toString.trim + "_" + sourceId.toString
              //从【数据快照字典】中抽取切片
              val snapshotDic = snapshotDicMap.getOrElse(snapshotDicKey, scala.collection.mutable.Map("None" -> "None"))
              //从【抽取切片】中，抽取【字典目标表快照字段信息】
              val dic = snapshotDic.getOrElse(reqTablename, List())
              //识别【字典目标表快照字段信息】是否符合要求格式
              type dicListSet = List[Map[String, Set[Any]]]
              val dicList: List[Map[String, Any]] = dic match {
                case diclistSet: dicListSet => diclistSet
                case _ => List(Map("Unknown data structure" -> Set()))
              }
              //println("dicList:" + dicList)
              var isCache = false
              if (dicList == List(Map("Unknown data structure" -> Set()))) println("探查入参有误，应该...，return...")
              else if ((dicList equals List()) && snapshotDic.contains(reqTablename)) {
                log.info("[find]探查到无周期表:" + reqTablename + " ApiId:" + getFinderApiId + " SourceId:" + sourceId)
                if (sourceId != 0) isCache = cacheDic.findInCache(reqTablename, TreeMap())
                val tbinfo = TableInfo(reqTablename, TreeMap(), sourceId, isCache)
                finderList = finderList :+ tbinfo
                break()
              }
              else {
                dicList.foreach((e: Map[String, Any]) => {
                  //【字典目标表快照字段信息】中，按Map段生成笛卡尔集
                  val dicTreeMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(e)
                  val thisDicList = cartesianProductStr(dicTreeMap.keySet.map(dicTreeMap).toList)
                  //求交集探查
                  val intersectList = thisDicList intersect fdlist
                  log.debug("thisDicList:" + thisDicList)

                  for (elem <- thisDicList.flatten) {
                    log.debug("thisDicList Type:" + elem.getClass)
                  }

                  log.debug("fdlist:" + fdlist)
                  for (elem <- fdlist.flatten) {
                    log.debug("thisDicList Type:" + elem.getClass)
                  }

                  println("intersectList:" + intersectList);log.debug("intersectList:" + intersectList)
                  //求差集，未探查到的
                  fdlist = fdlist diff thisDicList
                  //println("fdlist:" + fdlist);log.debug("fdlist:" + fdlist)
                  var finded = TreeMap[String, Any]()
                  var notfinded = TreeMap[String, Any]()
                  //如果探查出数据，则将结果写入返回集finderSnapshotMapSet
                  if (intersectList.nonEmpty) {
                    finded = redistributeMap(intersectList, reqPropTreeMap)

                    println("FFFFFFFFFFFF:" + finded)
                    println("查找是否需要缓存:" + reqTablename + " argMap:" + mapToTreeMapVallst(finded))

                    log.debug("查找是否需要缓存:" + reqTablename + " argMap:" + mapToTreeMapVallst(finded))
                    if (sourceId != 0) isCache = cacheDic.findInCache(reqTablename, mapToTreeMapVallst(finded))
                    log.debug("isCache:" + isCache)
                    val tbinfo = TableInfo(reqTablename, treeMapToMapVallst(finded), sourceId, isCache)
                    println("reqTablename:" + reqTablename)
                    println("Map:" + treeMapToMapVallst(finded))
                    log.info("[find]探查到有周期表:" + reqTablename + " 必选参数:" + treeMapToMapVallst(finded) + " ApiId:" + getFinderApiId + " SourceId:" + sourceId)
                    log.debug("tbinfo:" + tbinfo)
                    finderList = finderList :+ tbinfo
                    hitSet += getFinderApiId.toString.trim + "_" + getFinderApiVersion.toString.trim + "_" + sourceId
                    //println("hitSet:" + hitSet);log.debug("hitSet:" + hitSet)
                    //println("finderList:" + finderList);log.debug("finderList:" + finderList)
                  }
                  //如果探查集不为空，则继续探查；如果探查集为空，则退出探查，返回结果；
                  if (fdlist.nonEmpty) notfinded = redistributeMap(fdlist, reqPropTreeMap)
                  else break()
                })
              }
            }
          })
          //最终还有未探查到的结果，则返回false，没有未探查到的结果，则返回true
          if (fdlist.isEmpty) finderFlag = true
          if (!finderFlag) break()
          finderAllFlag = finderAllFlag & finderFlag
          log.info("finderFlag:" + finderFlag)
          log.info("finderAllFlag:" + finderAllFlag)
        })
      }
    })
    log.info("Final finderAllFlag:" + finderAllFlag)
    log.info("准备更新命中次数")
    if (finderAllFlag) hitSet.foreach(e => {
      //log.debug("Update the " + e + "hit_times + 1")
      val hit_dd_apiId = e.split("_")(0).toInt
      val hit_dd_apiVersion = e.split("_")(1).toInt
      val hit_dd_sourceId = e.split("_")(2).toInt
      DataSnapshotDic.apiDataSnpstDdDao.updateHitTimes(hit_dd_apiId, hit_dd_apiVersion, hit_dd_sourceId, None, Some(System.currentTimeMillis()))
    })
    log.info("准备发送数据位置信息：" + finderList)
    if (!finderAllFlag || finderList.isEmpty) None
    else Some(RetLocation(finderList))
  }

  def listSnapshot(getFinderApiId: Int, getFinderApiVersion: Int, columnName: String, getType: String): List[Any] = {
    val snapshotDicMap: Map[String, mutable.Map[String, Any]] = dataSnapshotMap.filterKeys(m => (m.split("_")(0).toInt == getFinderApiId) && m.split("_")(1).toInt == getFinderApiVersion) //APINAME -> 获取字典位置Map，从字典里获取该APINAME所对应的APINAME_SOURCEID组合所对应的快照信息
    var listContext: List[Any] = List()
    for (elemTableInfos <- snapshotDicMap) {
      for (elemColumnInfos <- elemTableInfos._2 if elemColumnInfos._1 equals columnName) {
        listContext = listContext :: ckList(elemColumnInfos._2)
      }
    }
    val retList: List[Any] = if (getType equals "MAX") List(mergedsortBySmallToBig(listContext).last)
    else if (getType equals "MIN") List(mergedsortBySmallToBig(listContext).head)
    else if (getType equals "ALL") mergedsortBySmallToBig(listContext)
    else List()
    retList
  }


}


object DataSnapshotDic {

  //var sourceids = List(1, 2, 3)

  //  多字段字典查询实现

  val apiNameSourceID1: String = "1001_1_1"
  //"testAPIhuzy_1"
  //Teradata001
  val api_nsry_arg1: String =
    """{"pmid3.tb_mid_par_tot_user_mon":[{"deal_date":[201601,201602],"region_code":["01","02","03","04","05"]},{"deal_date":[201601]}],"pmid3.tb_mid_fin_tot_user_mon":[{"deal_date":[201601,201602],"region_code":["01","02","03","04","05"]}]}"""


  val apiNameSourceID2: String = "1001_1_2"
  //"testAPIhuzy_2"
  //Teradata002
  val api_nsry_arg2: String =
    """{"pmid3.tb_mid_par_tot_user_mon":[{"deal_date":[201602,201603,201604],"region_code":["01","02","03","04","05","06"]}],"pmid3.tb_mid_fin_tot_user_mon":[{"deal_date":[201602,201603],"region_code":["01","02","03","04","05"]}]}"""

  val apiNameSourceID3: String = "1001_1_3"
  //"testAPIhuzy_3"
  //Aster001
  val api_nsry_arg3: String =
    """{"pmid3.tb_mid_par_tot_user_mon":[{"deal_date":[201601,201602,201603],"region_code":["01","02","03","04","05"]}],"pmid3.tb_mid_fin_tot_user_mon":[{"deal_date":[201601,201602,201603],"region_code":["01","02","03","04","05"]}]}"""

  val apiSource = new SourceInfoDao()
  val sourceids = apiSource.loadSourceIdList
  //val dataSnapshotDic: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map(apiNameSourceID1 -> api_nsry_arg1, apiNameSourceID2 -> api_nsry_arg2, apiNameSourceID3 -> api_nsry_arg3)
  val apiDataSnpstDdDao = ApiDataSnpstDdDao()
  val dataSnapshotDic: scala.collection.mutable.Map[String, String] = apiDataSnpstDdDao.loadSnpst

  //println("[dataSnapshotDic]:" + dataSnapshotDic)

  def apply() = {
    new DataSnapshotDic(sourceids, dataSnapshotDic)
  }


}

object DataSnapshotDicOperation extends DicMapFunc {

  def metaDataCollection[T: ClassTag](elems: T*) = mutable.Set[T](elems: _*)

  def main(args: Array[String]) {
    //初始化加载快照字典，需单例
    val a = DataSnapshotDic()
    println("加载快照字典:" + a.toString)

    //val startTime = System.currentTimeMillis()
    //for (i <- 1 to 100000){


    //模拟探查请求
    val getFinderApiId: Int = 1001 //"testAPIhuzy"
    val getFinderApiVersion: Int = 1
    //val mt1 = metaDataCollection(scala.collection.mutable.Map("deal_date" -> Set(201601, 201604), "region_code" -> Set("01", "02", "03", "04", "05", "06"))) ///20923
    //val mt1 = TreeMap("deal_date" -> List(201601, 201604), "region_code" -> List("01", "02", "03", "04", "05"))
    //val mt2 = TreeMap("deal_date" -> List(201601, 201602), "region_code" -> List("01", "02", "03", "04", "05"))

    //println("探查需求：" + mt1)
    val mt1 = TreeMap("DEAL_DATE" -> List(201604))
    var getFinderARG = mutable.Map[String, Any]()
    getFinderARG += ("RPTMART3.TB_RPT_BO_MON" -> mt1)
    //getFinderARG += ("pmid3.tb_mid_par_tot_user_mon" -> mt1)
    //getFinderARG += ("pmid3.tb_mid_fin_tot_user_mon" -> mt2)


    val getFinderARGS: List[mutable.Map[String, Any]] = metaDataCollection(getFinderARG).toList
    val location: Option[RetLocation] = a.find(getFinderApiId, getFinderApiVersion, getFinderARGS)
    println("location:" + location)

    val getFinderApiId2: Int = 1002 //"testAPIhuzy"
    val getFinderApiVersion2: Int = 1
    val mt2 = List()
    var getFinderARG2 = mutable.Map[String, Any]()
    getFinderARG2 += ("PMID3.TB_MID_PAR_USER_DAY" -> mt2)
    val getFinderARGS2: List[mutable.Map[String, Any]] = metaDataCollection(getFinderARG2).toList
    val location2: Option[RetLocation] = a.find(getFinderApiId2, getFinderApiVersion2, getFinderARGS2)
    println("location:" + location2)


    val getFinderApiId3: Int = 1002 //"testAPIhuzy"
    val getFinderApiVersion3: Int = 1
    val mt3 = List()
    var getFinderARG3 = mutable.Map[String, Any]()
    getFinderARG3 += ("PMID3.TB_MID_PAR_USER_MON" -> mt3)
    val getFinderARGS3: List[mutable.Map[String, Any]] = metaDataCollection(getFinderARG3).toList
    val location3: Option[RetLocation] = a.find(getFinderApiId3, getFinderApiVersion3, getFinderARGS3)
    println("location:" + location3)
    //val jsonLoal = generate(location)
    //println("jsonLoal:" + jsonLoal)
    //}
    //val endTime = System.currentTimeMillis()
    //println (endTime-startTime)
    //###############################################################################################################
    println("字典检查" + a.toString)


    val sourceId: Int = 0
    val tableName: String = "RPTMART3.TB_RPT_BO_MON1"
    //val updateApiCondtionMap : Map[String,AnyRef] = Map()  //Map[tableName:String -> Set[Map[columnName -> Set[columnValues]]]]

    val apiCondtionMap: String ="""{"DEAL_DATE":[201604,201605]}"""
      //"""{"DEAL_DATE":[201601,201602,201603]}"""
      //"""{"region_code":["01","02","03","04","05","06","07"],"DEAL_DATE":[201601,201602,201603]}"""
    val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(apiCondtionMap))
    println("jsonToTreeMap:" + jsonCondtionMap)

    val toTreeMapJson = generate(jsonCondtionMap)

    println("toTreeMapJson:" + toTreeMapJson)
    //a.add(sourceId, tableName, jsonCondtionMap)
    //a.drop(sourceId, tableName, apiCondtionMap)
    println("字典检查" + a.toString)
  }
}