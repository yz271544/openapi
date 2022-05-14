package com.teradata.openapi.framework.deploy


/**
  * Created by John on 2016/4/25.
  */
trait LocationInfo {

}

case class RetFormatFinger(formatFinger: String, file_loc: String) extends LocationInfo

case class RetDataFinger(dataFinger: String, file_loc: String, encode: String) extends LocationInfo

case class RetLocation(tableInfoList: List[TableInfo]) extends LocationInfo

case class TableInfo(tableFullName: String, columnMap: Map[String, List[Any]], sourceID: Int, isCach: Boolean)

/**
  *
  * @param schemaName
  * @param tableName
  * @param columnMap
  * @param sourceID
  * @param operateType = ADD 添加到缓存 ， DROP 从缓存清出
  */
case class CacheTable(schemaName: String, tableName: String, columnMap: Map[String, List[Any]], sourceID: Int, operateType: String)