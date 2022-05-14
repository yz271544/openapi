package com.teradata.openapi.formatPage

/**
  * Created by hdfs on 2016/6/21.
  */
class DeployType {
	private[formatPage] object Format extends Enumeration {
		type FormatType = Value
		val TXT, JSON, XML = Value
	}

	/**
	  * 数据编码类型
	  */
	private[formatPage] object Encode extends Enumeration {
		type EncodeType = Value
		val UTF8, GBK = Value
	}
}
