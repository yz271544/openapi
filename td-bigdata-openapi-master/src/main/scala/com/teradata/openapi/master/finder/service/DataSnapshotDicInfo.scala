package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{RetLocation, SorcType, TableInfo}

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.reflect.ClassTag
import com.teradata.openapi.framework.model._
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.cacher.service.DataSnpstCacheInfo
import com.teradata.openapi.master.finder.dao.{ApiCacheTabInfoDao, ApiDataSnpstDdInfoDao, ApiTabInfoDao}
import com.teradata.openapi.master.resolver.dao.SourceInfoDao

/**
  * Created by John on 2016/4/20.
  * apiTabinfos: mutable.Map[(Int, Int, Int), (String, String)]
  * context::::::::::Key:(ApiId,ApiVersion,SourceId)  -> Value:(SchenaName,TableName)
  * sourceSnapshotDic: mutable.Map[(Int, String, String), DataSnpstVal]
  * context::::::::::Key:(SourceId,SchemaName,TableName) -> Value:DataSnpstVal  -> DataSnpstVal(dataSnpstVal: List[ListDataSnpst])
  */
class DataSnapshotDicInfo(var sourceids: List[(Int, Boolean, Boolean, Boolean)], var apiTabinfos: mutable.Map[(Int, Int, Int), (String, String)],var sourceSnapshotDic: mutable.Map[(Int, String, String), DataSnpstVal])
  extends SnapshotDic with DicMapFunc with OpenApiLogging {
  val cacheDic = DataSnpstCacheInfo()
  val keys = sourceSnapshotDic.keySet
  if (sourceSnapshotDic.isEmpty) println("Sorry! This OpenAPI system haven't loaded data to service.")
  else {
    //查看快照字典Map的信息
    println("查看快照字典Map的信息:")
    log.debug("查看快照字典Map的信息:")
    println(toString)
  }

  override def toString = {
    //查看快照字典Map的信息
    println("apiTabinfos Map detail:" + apiTabinfos)
    println("sourceSnapshotDic Map detail:" + sourceSnapshotDic)


    var content = ""
    apiTabinfos.foreach(e => {
      val (k, v) = e
      val (apiId, apiVersion, sourceId) = k
      val (schenaName, tableName) = v
      val local = sourceSnapshotDic.getOrElse((sourceId, schenaName, tableName), List())
      content = content + " Key:" + apiId + "_" + apiVersion + "_" + sourceId + " -> " + " Value:" + schenaName + "." + tableName + " local:" + local + "\n"
    })
    content
  }

  def reloadSourceIds() = {
    val apiSource = new SourceInfoDao()
    sourceids = apiSource.loadSourceIdListNew
  }

  def reloadApiTabinfos() = {
    val apiTabInfoDao = ApiTabInfoDao()
    apiTabinfos = apiTabInfoDao.load
    val apiCacheTabInfoDao = ApiCacheTabInfoDao()
    var apiCacheTabInfos: Seq[ApiCacheTabInfoRow] = apiCacheTabInfoDao.loadCache
    for (elem <- apiCacheTabInfos) {
      apiTabinfos += ((elem.api_id, elem.api_version, elem.source_id) ->(elem.schema_name, elem.tab_name))
    }
    apiCacheTabInfos = Nil
  }

  def reloadSourceSnapshotDic() = {
    sourceSnapshotDic = ApiDataSnpstDdInfoDao().loadSnpst
  }

  def reFreshApiTabInfos(apiId: Int, apiVersion: Int): Int = {
    var result = 0
    //println("Old apiTabinfos Map:" + apiTabinfos)
    try {
      val apiTabInfoDao = ApiTabInfoDao()
      val apiTabinfos_new: mutable.Map[(Int, Int, Int), (String, String)] = apiTabInfoDao.loadByApiIdApiVersion(apiId, apiVersion)
      //apiTabinfos = apiTabinfos_new
      log.debug("reloadApiTabInfos: sourceids:" + sourceids)
      println("reloadApiTabInfos: sourceids:" + sourceids)

      for (sourceIdelem <- sourceids) {
        apiTabinfos.-=((apiId, apiVersion, sourceIdelem._1))
      }
      //println("清除"+ apiId + " v:" + apiVersion +" apiTabinfos:" + apiTabinfos
      //println("要增加" + apiTabinfos_new)
      apiTabinfos.++=(apiTabinfos_new)
    } catch {
      case e: Exception => {
        log.error("Refresh the api table relation error!" + e.printStackTrace())
        result = 1
      }
    }
    //log.debug("Refresh apiTabinfos Map:" + apiTabinfos)
    //println("Refresh apiTabinfos Map:" + apiTabinfos)
    result
  }

  /**
    *
    * @param sourceId
    * @param schemaName
    * @param tableName
    * @param jsonCondtionMap
    * @return List[(ApiId:Int, ApiVersion:Int)]
    */
  def add(sourceId: Int, schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]]): List[(Int, Int)] = {
    val schemaNameUpper = schemaName.toUpperCase
    val tableNameUpper = tableName.toUpperCase
    val jsonCondtionMapUpperKey = upperTreeMapKey(jsonCondtionMap)
    //log.debug("DataSnapshotDic add: sourceId: " + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper + " jsonCondtionMap:" + jsonCondtionMapUpperKey)
    //println("DataSnapshotDic add: sourceId: " + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper + " jsonCondtionMap:" + jsonCondtionMapUpperKey)

    //格式校验
    //val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(condtionMap))
    //更新字典
    val dataSnpstValOfDic: Option[DataSnpstVal] = sourceSnapshotDic.get(sourceId, schemaNameUpper, tableNameUpper)
    val realDataSnpstValOfDic: DataSnpstVal = dataSnpstValOfDic.getOrElse(DataSnpstVal(List()))

    //println("dataSnpstValOfDic:" + realDataSnpstValOfDic.dataSnpstVal)
    var listdataSnpstDic: List[DataSnpst] = List[DataSnpst]()
    for (elem <- jsonCondtionMapUpperKey) {
      listdataSnpstDic = listdataSnpstDic.::(DataSnpst(elem._1, elem._2))
    }
    val addRet: DataSnpstVal = DataSnpstVal(realDataSnpstValOfDic.add(listdataSnpstDic))
    sourceSnapshotDic += ((sourceId, schemaNameUpper, tableNameUpper) -> addRet)
    //println("sourceSnapshotDic ToString:" + sourceSnapshotDic.toString)
    //println("sourceSnapshotDic:" + sourceSnapshotDic)
    //println("upsert:sourceId:" + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper + " DataSnpstVal:" + addRet)
    DataSnapshotDicInfo.apiDataSnpstDdInfoDao.upsertSnpst(sourceId, schemaNameUpper, tableNameUpper, addRet)
    //println(DataSnapshotDicInfo.apiDataSnpstDdInfoDao.upsertSnpstStatment(sourceId,schemaName,tableName,addRet))
    //查找增加了周期信息，所能影响的API、Version清单
    val updApiIdList: List[(Int, Int)] = findTriggerApis(sourceId, schemaNameUpper, tableNameUpper)
    //println("updApiIdList:::" + updApiIdList)
    for (elem <- updApiIdList) {
      //println("updApiIdListElem:" + elem)
      apiTabinfos += ((elem._1, elem._2, sourceId) ->(schemaNameUpper, tableNameUpper))
      //println("updateCacheTableInfo:" + elem._1 + "," + elem._2 + "," + sourceId + "," + schemaNameUpper + "," + tableNameUpper)
      //log.debug("updateCacheTableInfo:" + elem._1 + "," + elem._2 + "," + sourceId + "," + schemaNameUpper + "," + tableNameUpper)
      DataSnapshotDicInfo.apiCacheTabInfoDao.upsertCache(elem._1, elem._2, sourceId, schemaNameUpper, tableNameUpper)
      //println("updateAfter:" + elem._1 + "," + elem._2 + "," + sourceId + "," + schemaNameUpper + "," + tableNameUpper)
      //log.debug("updateAfter:" + elem._1 + "," + elem._2 + "," + sourceId + "," + schemaNameUpper + "," + tableNameUpper)

    }
    updApiIdList
  }

  /**
    * 寻找需要触发影响的List(ApiId :Int,ApiVersion :Int)
    *
    * @param sourceId
    * @param schemaName
    * @param tableName
    * @return List(ApiId :Int,ApiVersion :Int)
    */
  def findTriggerApis(sourceId: Int, schemaName: String, tableName: String): List[(Int, Int)] = {
    var updApiIdList = List[(Int, Int)]()
    this.apiTabinfos.foreach(e => {
      val (k, v) = e
      val (tabApiId, tabApiVersion, tabSourceId) = k
      val (tabSchemaName, tabTableName) = v
      //println("影响的API、Version:[tabApiId:" + tabApiId + " tabApiVersion:" + tabApiVersion + " tabSourceId:" + tabSourceId + " tabSchemaName:" + tabSchemaName + " tabTableName:" + tabTableName + "]")
      if (tabSourceId == sourceId && (tabSchemaName equals schemaName) && (tabTableName equals tableName))
        updApiIdList = updApiIdList.::(tabApiId, tabApiVersion)
    })
    updApiIdList
  }

  //返回更新了数据的记录数
  def drop(sourceId: Int, schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]]): Int = {
    val schemaNameUpper = schemaName.toUpperCase
    val tableNameUpper = tableName.toUpperCase
    val jsonCondtionMapUpperKey = upperTreeMapKey(jsonCondtionMap)
    //log.debug("DataSnapshotDic drop: sourceId: " + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper + " jsonCondtionMap:" + jsonCondtionMapUpperKey)
    //println("DataSnapshotDic drop: sourceId: " + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper + " jsonCondtionMap:" + jsonCondtionMapUpperKey)
    //更新字典
    val dataSnpstValOfDic: Option[DataSnpstVal] = sourceSnapshotDic.get(sourceId, schemaNameUpper, tableNameUpper)
    val realDataSnpstValOfDic: DataSnpstVal = dataSnpstValOfDic.getOrElse(DataSnpstVal(List()))

    //println("dataSnpstValOfDic:" + realDataSnpstValOfDic.dataSnpstVal)
    var listdataSnpstDic: List[DataSnpst] = List[DataSnpst]()
    for (elem <- jsonCondtionMapUpperKey) {
      listdataSnpstDic = listdataSnpstDic.::(DataSnpst(elem._1, elem._2))
    }
    //删除完之后的剩余值
    val dropRetAfter: DataSnpstVal = DataSnpstVal(realDataSnpstValOfDic.drop(listdataSnpstDic))
    sourceSnapshotDic += ((sourceId, schemaNameUpper, tableNameUpper) -> dropRetAfter)
    //println("sourceSnapshotDic ToString:" + sourceSnapshotDic.toString)
    //println("sourceSnapshotDic:" + sourceSnapshotDic)
    //println("upsert:sourceId:" + sourceId + " schemaName:" + schemaNameUpper + " tableName:" + tableNameUpper)
    //println("DataSnpstVal:" + dropRetAfter)

    //如果探查的字段为空，字典信息也为空，则认为该表为无周期字段表，直接删除该记录
    if (jsonCondtionMap.isEmpty && realDataSnpstValOfDic.dataSnpstVal.isEmpty)
      DataSnapshotDicInfo.apiDataSnpstDdInfoDao.delSnpst(sourceId, schemaName, tableName)
    else DataSnapshotDicInfo.apiDataSnpstDdInfoDao.upsertSnpst(sourceId, schemaName, tableName, dropRetAfter)
  }

  def find(isSyn: Int, getFinderApiId: Int, getFinderApiVersion: Int, reqArgs: List[scala.collection.mutable.Map[String, Any]], isStartHiveCacheFlag: Boolean): Option[RetLocation] = {

    val typedSourceids: List[Int] = isSyn match {
      case 0 => for (sourceid <- sourceids if sourceid._2) yield sourceid._1
      case 1 => for (sourceid <- sourceids if sourceid._3) yield sourceid._1
      case 2 => for (sourceid <- sourceids if sourceid._4) yield sourceid._1
      case _ => List()
    }

    println("typedSourceids:" + typedSourceids)

    var retTabInfoList: List[TableInfo] = List[TableInfo]()
    for (elemReqArg: mutable.Map[String, Any] <- reqArgs) {
      for (elem: (String, Any) <- elemReqArg) {
        val Array(getSchemaName: String, getTableName: String) = elem._1.split('.')
        val reqPropTreeMap: TreeMap[String, List[Any]] = upperTreeMapKey(mapToTreeMapVallst(ckMap(elem._2)))
        log.debug("findByTableColInfo args:" + typedSourceids + " getFinderApiId:" + getFinderApiId + " getFinderApiVersion:" + getFinderApiVersion + " getSchemaName:" + getSchemaName.toUpperCase + " getTableName:" + getTableName.toUpperCase + " reqPropTreeMap:" + reqPropTreeMap + " isStartHiveCacheFlag" + isStartHiveCacheFlag)
        val retLocationTbInfo: Option[RetLocation] = findByTableColInfo(typedSourceids, getFinderApiId, getFinderApiVersion, getSchemaName.toUpperCase, getTableName.toUpperCase, reqPropTreeMap, isStartHiveCacheFlag)
        println("retLocationTbInfo:" + retLocationTbInfo)
        if (retLocationTbInfo.nonEmpty) {
          val ele: List[TableInfo] = retLocationTbInfo.get.tableInfoList
          retTabInfoList = retTabInfoList ::: ele
        }
      }
    }
    if (retTabInfoList.isEmpty) None
    else Some(RetLocation(retTabInfoList))
  }


  def findByTableColInfo(typedSourceids: List[Int], getFinderApiId: Int, getFinderApiVersion: Int, getSchemaName: String, getTableName: String, jsonCondtionMap: TreeMap[String, List[Any]], isStartHiveCacheFlag: Boolean): Option[RetLocation] = {

    /*println("DataSnapshotDIcInfo: jsonCondtionMap:::::::::::" + jsonCondtionMap)
    for (elemValueList <- jsonCondtionMap.values) {
      for (elem <- elemValueList) {
        println("jsonCondtionMap DATA TYPE ::::::::::: " + elem.getClass)
      }
    }*/

    //println("sourceIDs:" + sourceids)
    //将待探查的周期字段和值，装箱到List[DataSnpst]中
    var retTabInfoList: List[TableInfo] = List[TableInfo]() //放找到的
    var listDataSnpst: List[DataSnpst] = treeMap2ListDataSnpst(jsonCondtionMap) //放准备找、未找到的
    //log.debug("findByTableColInfo ----> listDataSnpst:" + listDataSnpst)
    for (elemSouceId: Int <- typedSourceids if listDataSnpst.nonEmpty) {
      //println("------------Start Source is:" + elemSouceId + "------------------------------------------------------------------------------------------------------------")
      val nullFlag: Boolean = isNullFlag(listDataSnpst)
      //log.debug("     ******* listDataSnpst:" + listDataSnpst + "***************** nullFlag:" + nullFlag)
      //println("     ******* listDataSnpst:" + listDataSnpst + "***************** nullFlag:" + nullFlag)
      var isCache = false
      //查字典
      val dataSnpstOfDicVal: DataSnpstVal = sourceSnapshotDic.getOrElse((elemSouceId, getSchemaName, getTableName), DataSnpstVal(List()))
      //log.debug("findByTableColInfo ----> dataSnpstOfDicVal:" + dataSnpstOfDicVal)
      //println("findByTableColInfo ----> dataSnpstOfDicVal:" + dataSnpstOfDicVal)

      //判断如果字典里存在该表信息
      if (sourceSnapshotDic.contains((elemSouceId, getSchemaName, getTableName))) {
        //log.debug("sourceSnapshotDic contains the Table elemSouceId:" + elemSouceId + " getSchemaName:" + getSchemaName + " getTableName:" + getTableName)
        //println("sourceSnapshotDic contains the Table elemSouceId:" + elemSouceId + " getSchemaName:" + getSchemaName + " getTableName:" + getTableName)

        //如果探查结果的dataSnpstVal内容为空，如果探查结果对象的dataSnpstVal内容也为空，则说明该表为无周期字段表，判断是否要进入缓存，然后返回
        //log.debug("dataSnpstOfDicVal.dataSnpstVal:" + dataSnpstOfDicVal.dataSnpstVal)
        //println("dataSnpstOfDicVal.dataSnpstVal:" + dataSnpstOfDicVal.dataSnpstVal)

        if (listIsNullFlag(dataSnpstOfDicVal.dataSnpstVal)) {
          //log.debug("findByTableColInfo ---> 无周期字段表")
          //println("findByTableColInfo ---> 无周期字段表")
          //判断是否需要缓存   ******暂时去掉进入Hive缓存步骤，全部传false
          if (isStartHiveCacheFlag && elemSouceId != 0) isCache = cacheDic.findInCache(getSchemaName + "." + getTableName, TreeMap())
          //说明该表为无周期字段表，返回值如下
          retTabInfoList = retTabInfoList :+ TableInfo(getSchemaName + "." + getTableName, TreeMap(), elemSouceId, isCache)
        } else {
          //如下为有周期字段表探查
          var unFounded: List[DataSnpst] = List()
          //breakable {
          //从字段、值Map参数中，抽取参数构建DataSnpst对象，并串成ListDataSnpst对象
          //println("探索值或探索剩余值：" + listDataSnpst)
          //返回为Tuple类型，_.1：代表查找到的值  _.2：代表未查找到的值
          val findSoucVals: (List[DataSnpst], List[DataSnpst]) = dataSnpstOfDicVal.find(ListDataSnpst(listDataSnpst))
          //log.debug("findByTableColInfo ----> findSoucVals:" + findSoucVals)
          //判断未查找到的值是否为空
          val founded: List[DataSnpst] = findSoucVals._1
          //println("Founded is:" + founded)
          unFounded = findSoucVals._2
          //println("UnFounded is:" + unFounded)
          //如果找到了
          if (!isNullFlag(founded)) {
            val reqPropTreeMap: TreeMap[String, List[Any]] = listDataSnpst2TreeMap(founded)
            //判断是否需要缓存   ******暂时去掉进入Hive缓存步骤，全部传false
            if (isStartHiveCacheFlag && elemSouceId != 0) isCache = cacheDic.findInCache(getSchemaName + "." + getTableName, reqPropTreeMap)
            val tbInfo = TableInfo(getSchemaName + "." + getTableName, reqPropTreeMap, elemSouceId, isCache)
            //log.debug(" IIIIIIIIII tbInfo:" + tbInfo + "    IIIIIIIIIIIIIIIII")
            retTabInfoList = retTabInfoList :+ tbInfo
            //log.debug(" 本质 ：" + retTabInfoList)
          }

          val d: Boolean = !isNullFlag(unFounded)
          //log.debug("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU             " + unFounded + " 未查找到的不为空:" + d)
          //if (!isNullFlag(unFounded)) {
          //未查找到的值不为空，需到下个数据源里去探查是否存在
          listDataSnpst = unFounded
          //  break()
          //}
          //}
        }
      }
      //给山西移动设置默认Teradata主仓库 -----BUG ERROR modify by Huzy
      /*else {
        retTabInfoList = retTabInfoList :+ TableInfo(getSchemaName + "." + getTableName, listDataSnpst.map(i => (i.columnName,i.columnValues)).toMap, 1, isCach = false)
        log.debug("defaultRetTabInfoList:" + retTabInfoList)
        listDataSnpst = List()
      }*/
    }

    val retLocation: Option[RetLocation] =
      if (!isNullFlag(listDataSnpst)) //None
      { //给山西移动设置默认Teradata主仓库
        val defaultTabInfoList: List[TableInfo] = retTabInfoList.map(i => TableInfo(i.tableFullName,i.columnMap,1,i.isCach))
        log.debug("defaultTabInfoList:" + defaultTabInfoList)
        Some(RetLocation(defaultTabInfoList))
      }
      else Some(RetLocation(retTabInfoList))

    //val retLocation: Option[RetLocation] = Some(RetLocation(retTabInfoList))
    //println("findByTableColInfo:" + aa)
    retLocation
  }

  /**
    * 判断List[DataSnpst]是否为空
    *
    * @param findRet
    * @return
    */
  def isNullFlag(findRet: List[DataSnpst]): Boolean = {
    val isNullFlag = findRet.map(_.columnValues.isEmpty).aggregate(true)(_ & _, _ & _)
    log.debug("isNullFlag:" + isNullFlag)
    isNullFlag
  }

  /**
    * 判断List[ListDataSnpst]是否为空
    * @param findRet
    * @return
    */
  def listIsNullFlag(findRet: List[ListDataSnpst]): Boolean = {
    var isNull :Boolean = true
    for (elem: ListDataSnpst <- findRet) {
      isNull = isNull && isNullFlag(elem.listdataSnpstDic)
    }
    isNull
  }

  //查询罗列快照信息
  /**
    * apiTabinfos: mutable.Map[(Int, Int, Int), (String, String)]
    * context::::::::::Key:(ApiId,ApiVersion,SourceId)  -> Value:(SchenaName,TableName)
    * sourceSnapshotDic: mutable.Map[(Int, String, String), DataSnpstVal]
    * context::::::::::Key:(SourceId,SchemaName,TableName) -> Value:DataSnpstVal  -> DataSnpstVal(dataSnpstVal: List[ListDataSnpst])
    *
    * @param getFinderApiId
    * @param getFinderApiVersion
    * @param columnName
    * @param getType
    * @return
    */
  def listSnapshot(getFinderApiId: Int, getFinderApiVersion: Int, columnName: String, getType: String): List[Any] = {
    //APINAME -> 获取字典位置Map，从字典里获取该APINAME所对应的APINAME_SOURCEID组合所对应的快照信息
    var dataSnpstValObjList: List[DataSnpstVal] = List[DataSnpstVal]()
    apiTabinfos.foreach(e => {
      val (k, v) = e
      val (apiId, apiVersion, sourceId) = k
      val (schenaName, tableName) = v
      if (apiId == getFinderApiId && apiVersion == getFinderApiVersion) {
        val local: DataSnpstVal = sourceSnapshotDic.getOrElse((sourceId, schenaName, tableName), DataSnpstVal(List()))
        if (local.dataSnpstVal.nonEmpty) {
          dataSnpstValObjList = dataSnpstValObjList :+ local
        }
      }
    })
    var listContext: List[Any] = List()
    for (elem: DataSnpstVal <- dataSnpstValObjList) {
      for (elem: ListDataSnpst <- elem.dataSnpstVal) {
        for (elem: DataSnpst <- elem.listdataSnpstDic) {
          if (elem.columnName equals columnName) {
            //log.debug("ELEM:" + elem.columnName + " elemValue:" + elem.columnValues)
            //println("ELEM:" + elem.columnName + " elemValue:" + elem.columnValues)
            //val roundList = for (e <- elem.columnValues ) yield if (e.isInstanceOf[Double]) Math.floor(e).asInstanceOf[Int] else e
            listContext = (listContext ::: elem.columnValues).distinct
            //println("FOR IN listContext:" + listContext)
          }
        }
      }
    }
    //listContext = listContext.flatten
    //log.debug("listContext:" + listContext)
    //println("listContext:" + listContext)
    val retList: List[Any] =
      if (listContext.nonEmpty) {
        if (getType.toUpperCase equals "MAX") List(mergedsortBySmallToBig(listContext).last)
        else if (getType.toUpperCase equals "MIN") List(mergedsortBySmallToBig(listContext).head)
        else if (getType.toUpperCase equals "ALL") mergedsortBySmallToBig(listContext)
        else List()
      }
      else List()
    retList
  }

  def listTables(isSyn: Int, apiId: Int, apiVersion: Int): List[SorcType] = {
    var listSoucType: List[SorcType] = List()
    val typedSourceids: List[Int] = isSyn match {
      case 0 => for (sourceid <- sourceids if sourceid._2) yield sourceid._1
      case 1 => for (sourceid <- sourceids if sourceid._3) yield sourceid._1
      case 2 => for (sourceid <- sourceids if sourceid._4) yield sourceid._1
      case _ => List()
    }

    log.debug("listTables ------ isSyn:" + isSyn + " apiId:" + apiId + " apiVersion:" + apiVersion + " typedSourceids:" + typedSourceids)
    for (elemSourceId <- typedSourceids) {
      val retApiTabInfos = apiTabinfos.get((apiId, apiVersion, elemSourceId))
      log.debug("retApiTabInfos:" + retApiTabInfos)
      if (retApiTabInfos.nonEmpty){
        val schemaName = retApiTabInfos.get._1
        val tabName = retApiTabInfos.get._2
        listSoucType = listSoucType :+ SorcType(elemSourceId, schemaName, tabName, "T1", sorc_field_type = "", sorc_format = "", sorc_max_len = 0, sorc_prec_digit = 0, sorc_total_digit = 0)
      }
    }
    log.debug("ret listSoucType : " + listSoucType)
    listSoucType
  }
}


object DataSnapshotDicInfo {

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
  val sourceids: List[(Int, Boolean, Boolean, Boolean)] = apiSource.loadSourceIdListNew
  //val dataSnapshotDic: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map(apiNameSourceID1 -> api_nsry_arg1, apiNameSourceID2 -> api_nsry_arg2, apiNameSourceID3 -> api_nsry_arg3)
  val apiTabInfoDao = ApiTabInfoDao()
  val apiTabinfos: mutable.Map[(Int, Int, Int), (String, String)] = apiTabInfoDao.load
  val apiCacheTabInfoDao = ApiCacheTabInfoDao()
  var apiCacheTabInfos: Seq[ApiCacheTabInfoRow] = apiCacheTabInfoDao.loadCache
  for (elem <- apiCacheTabInfos) {
    apiTabinfos += ((elem.api_id, elem.api_version, elem.source_id) ->(elem.schema_name, elem.tab_name))
  }
  apiCacheTabInfos = Nil


  val apiDataSnpstDdInfoDao = ApiDataSnpstDdInfoDao()
  val dataSnapshotDic: mutable.Map[(Int, String, String), DataSnpstVal] = apiDataSnpstDdInfoDao.loadSnpst


  //println("[dataSnapshotDic]:" + dataSnapshotDic)

  def apply() = {
    new DataSnapshotDicInfo(sourceids, apiTabinfos, dataSnapshotDic)
  }

}

object DataSnapshotDicInfoOperation extends DicMapFunc {

  def metaDataCollection[T: ClassTag](elems: T*) = mutable.Set[T](elems: _*)

  def main(args: Array[String]) {
    //初始化加载快照字典，需单例
    val a = DataSnapshotDicInfo()
    //a.reFreshApiTabInfos(1001,1)
    /*println("加载快照字典:" + a.toString)

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
    println("location:" + location3)*/
    //val jsonLoal = generate(location)
    //println("jsonLoal:" + jsonLoal)
    //}
    //val endTime = System.currentTimeMillis()
    //println (endTime-startTime)
    //###############################################################################################################
//    println("字典检查:\n" + a.toString)


    /*val sourceId: Int = 1
    val schemaName: String = """RPTMART3"""
    val tableName: String = """TB_RPT_BO_MON"""
    //val updateApiCondtionMap : Map[String,AnyRef] = Map()  //Map[tableName:String -> Set[Map[columnName -> Set[columnValues]]]]

    val apiCondtionMap: String ="""{"DEAL_DATE":["2015-04-30"]}"""
    //"""{"DEAL_DATE":[201606],"REGION_CODE":["05","08"]}"""
    //"""{"DEAL_DATE":[201601,201602,201603]}"""
    //val apiCondtionMap: String = """{"REGION_CODE":["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"],"DEAL_DATE":[201606]}"""
    val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(apiCondtionMap))
    println("jsonToTreeMap:" + jsonCondtionMap)

    val toTreeMapJson: String = generate(jsonCondtionMap)

    println("toTreeMapJson:" + toTreeMapJson)
    //a.add(sourceId, schemaName, tableName, jsonCondtionMap)
    //a.drop(sourceId, schemaName, tableName, jsonCondtionMap)
    */
    val getFinderApiId: Int = 1001 //"testAPIhuzy"
    val getFinderApiVersion: Int = 1
    //val mt1 = metaDataCollection(scala.collection.mutable.Map("deal_date" -> Set(201601, 201604), "region_code" -> Set("01", "02", "03", "04", "05", "06"))) ///20923
    //val mt1 = TreeMap("deal_date" -> List(201601, 201604), "region_code" -> List("01", "02", "03", "04", "05"))
    //val mt2 = TreeMap("deal_date" -> List(201601, 201602), "region_code" -> List("01", "02", "03", "04", "05"))


    //getFinderARG += ("pmid3.tb_mid_par_tot_user_mon" -> mt1)
    //getFinderARG += ("pmid3.tb_mid_fin_tot_user_mon" -> mt2)

    println("--------------FIND ----------------------------------------------------------------------------------------------")

    val mt1 = TreeMap("DEAL_DATE" -> List("201604"))
    var getFinderARG = mutable.Map[String, Any]()
    getFinderARG += ("RPTMART3.TB_RPT_BO_MON" -> mt1)

    val getFinderARGS: List[mutable.Map[String, Any]] = metaDataCollection(getFinderARG).toList
    println("要探查:" + getFinderARGS)
    val fd: Option[RetLocation] = a.find(isSyn = 0, getFinderApiId, getFinderApiVersion, getFinderARGS, isStartHiveCacheFlag = false)
    println("Founed local : " + fd)

    /*println("--------------DROP ----------------------------------------------------------------------------------------------")
    val dropApiCondtionMap: String ="""{"deal_date":[201606],"region_code":["01"]}"""
    val dropJsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(dropApiCondtionMap))

    val ddp: Int = a.drop(1, "rptmart3", "TB_RPT_BO_MON", dropJsonCondtionMap)

    /*
            println("--------------ADD ----------------------------------------------------------------------------------------------")
            val addApiCondtionMap: String ="""{"DEAL_DATE":["2015-04-30"]}"""
            val addJsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(addApiCondtionMap))
            val adp: List[(Int, Int)] = a.add(0, "PMID3", "TB_MID_LOC_CMCC_USER_201504", addJsonCondtionMap)
        */


    println("--------------字典检查----------------------------------------------------------------------------------------------")
    println("字典检查" + a.toString)*/

    /*println("-------------- list snapshot ----------------------------------------------------------------------------------------------")

    val listSnps = a.listSnapshot(1001,1,"DEAL_DATE","MIN")
    println(listSnps)*/

    /*println("------------------- 刷新API和表的关系 -----------------------------------------------------------------------------------------------------")
    a.reloadApiTabInfos(1001, 1)*/
  }
}