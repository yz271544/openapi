package com.teradata.openapi.framework.step

import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.model.SourceInfoRow

/**
  * Created by lzf on 2016/3/31.
  */
trait Step extends Serializable {


}

trait AsynStep extends Step

/*
trait SyncStep extends Step {
  def tableInfo: TableInfo

  def source: SourceInfoRow
  def reqID:String
  def formatFinger:String
  def clientPath:String
}*/

//取数步骤
case class FetchStep(source: SourceInfoRow,
                     tableInfo: TableInfo,
                     reqArgs: List[ReqArg],
                     repArgs: List[RepArg],
                     retDataFinger: String) extends AsynStep {

  def outFileName: String = {
    s"$retDataFinger"
  }

  override def toString: String = {
    s"Fetch data from source: ${tableInfo.sourceID}, table: ${tableInfo.tableFullName} to file:  $outFileName"
  }

}

//缓存步骤
case class CacheStep(source: SourceInfoRow, tableInfo: TableInfo) extends AsynStep {

  def cacheTableName: String = {
    "cache_tb_test"
  }

  override def toString: String = {
    s"Cache data from source: ${tableInfo.sourceID}, table: ${tableInfo.tableFullName} to cache"
  }

  override def equals(that: Any): Boolean = that match {
    case CacheStep(row, info) =>
      this.source == row &&
        this.tableInfo.tableFullName == info.tableFullName &&
        this.tableInfo.columnMap == info.columnMap
    case _ => false
  }

}

//格式封装步骤
case class FormatStep(format: Format,
                      encode: Encode.Value,
                      repArgs: List[RepArg],
                      retDataFinger: String,
                      retFormatFinger: String
                     ) extends AsynStep {

  def inFileName: String = {
    s"$retDataFinger"
  }

  def outFileName: String = {
    s"$retFormatFinger"
  }

  override def toString: String = {
    s"Transform data from $inFileName to: $outFileName using format: $format encode: $encode"
  }

}

//推送步骤
case class PushStep(retFormatFinger: String, format: Option[Format], pushArgs: Option[PushArgs]) extends AsynStep {

  override def toString: String = {
    "Push data  ..."
  }
}

//清理步骤
case class CleanStep(cleanTables: List[CacheTable]) extends AsynStep {

  override def toString: String = {
    "Clean data  ..."
  }
}

//清理文件步骤
case class CleanFileStep(finger: List[String]) extends AsynStep {

  override def toString: String = {
    "Clean file  ..."
  }
}

//同步取数步骤
case class SyncStep(reqID: String,
                    source: SourceInfoRow,
                    tableInfo: TableInfo,
                    reqArgs: List[ReqArg],
                    repArgs: List[RepArg],
                    pushArgs: Option[PushArgs],
                    formatFinger: String,
                    format: Format,
                    encode: Encode.Value,
                    clientPath: String,
                    pageSize: Option[Int],
                    pageNum: Option[Int]) extends Step {

  override def toString: String = {
    "Syn data  ..."
  }
}

//测试Worker与推送FTP服务器的连通性
case class FtpTest(pushPluginType: String, host: String, port: Int, username: String, password: String, ftpmode: String, directory: String) extends Step {
  override def toString: String = {
    s"FtpTest[$pushPluginType] -> $host:$port user:$username password:$password ftpmode: $ftpmode dir:$directory"
  }
}