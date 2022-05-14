package com.teradata.openapi.framework.util.pluginUtil

import java.sql.ResultSet

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{Encode, SorcType}

import scala.collection.mutable

/**
  * Created by hdfs on 2017/5/16.
  */
private[openapi] object TypeConvertUtil extends OpenApiLogging{

	/**
	  * Get sorcTypeType
	  * @param source_id
	  * @param st
	  * @return
	  */
	def getSorcTypeType(source_id: Int, st: SorcType): String = st match {
		case SorcType(source_id, _, _, _, f, _, _, _, _) => f
		case _ => null
	}


	/**
	  *
	  * @param columnType
	  * @return
	  */
	def hivePartValueString(columnType: String): String = {
		columnType match {
			case "DATE" => "'%s'"
			case "TIMESTAMP" => "'%s'"
			case "CHAR" | "STRING" | "VARCHAR" => "'%s'"
			case "DECIMAL" | "BINARY" | "TINYINT" | "FLOAT" | "BIGINT" | "DOUBLE" | "INT" | "SMALLINT" => "%s"
			case "BOOLEAN" => "%s"
			case _ => "%s"
		}
	}

/*	/**
	  * sid 不合适，应该用source_type 注释当前函数 2017-09-05 10:17
	  * @param sid						数据源ID
	  * @param sl						数据源常量值List
	  * @param sorc_field_type		数据源字段类型
	  * @return						返回限制字符串
	  */
	def reqCvtCondition(sid: Int)(sl: List[Any])(sorc_field_type: String): String = {
		val re = limitedField(sl) _
		sid match {
			//tot 37	TD
			case 1 => sorc_field_type match {
				case "DA" 				//1
				=> re("Tdate")
				case "SZ" | "TS" 		//2
				=> re("timestamp")
				case "AT" | "BO" | "CF" | "CO" | "CV" | "GF" | "GV" | "PD" | "PM" | "PS" | "PT" | "PZ" | "UT" //13
				=> re("string")
				case "BF" | "BV" | "D" | "DH" | "DM" | "DS" | "DY" | "F" | "HM" | "HR" | "HS" | "I1" | "I2" | "I" | "MI" | "MO" | "MS" | "SC" | "TZ" | "YM" | "YR" //21
				=> re("value")
				case _ => re("value")
			}
			//tot 22 Aster
			case 2 => sorc_field_type match {
				case "date" //1
				=> re("date")
				case "text" | "character" | "varchar" | "time without time zone" | "time with time zone" | "uuid" | "ip4" | "ip4range" //8
				=> re("string")
				case "timestamp without time zone" | "timestamp with time zone" //2
				=> re("timestamp")
				case "bool" | "bytea" | "bigint" | "smallint" | "integer" | "real" | "double precision" | "interval" | "bit" | "varbit" | "numeric" //11
				=> re("value")
				case _ => re("value")
			}
			// hive
			case 0 => sorc_field_type match {
				case "DATE" => re("date")
				case "CHAR" | "STRING" | "VARCHAR" => re("string")
				case "TIMESTAMP" => re("string")
				case "DECIMAL" | "BINARY" | "TINYINT" | "FLOAT" | "BIGINT" | "DOUBLE" | "INT" | "SMALLINT" => re("value")
				case "BOOLEAN" => re("value")
				case _ => re("value")
			}

		}
	}	*/

	/**
	  * 修改sid 为stp 数据源 类型
	  * @param sid						数据源ID
	  * @param sl						数据源常量值List
	  * @param sorc_field_type		数据源字段类型
	  * @return						返回限制字符串
	  */
	def reqCvtCondition(stp: String)(sl: List[Any])(sorc_field_type: String): String = {
		val re = limitedField(sl) _
		stp match {
			//tot 37	TD
			case "01" => sorc_field_type.toUpperCase match {
				case "DA" 				//1
				=> re("TDATE")
				case "SZ" | "TS" 		//2
				=> re("TIMESTAMP")
				case "AT" | "BO" | "CF" | "CO" | "CV" | "GF" | "GV" | "PD" | "PM" | "PS" | "PT" | "PZ" | "UT" //13
				=> re("STRING")
				case "BF" | "BV" | "D" | "DH" | "DM" | "DS" | "DY" | "F" | "HM" | "HR" | "HS" | "I1" | "I2" | "I" | "MI" | "MO" | "MS" | "SC" | "TZ" | "YM" | "YR" //21
				=> re("VALUE")
				case _ => re("VALUE")
			}
			//tot 22 Aster
			case "02" => sorc_field_type.toUpperCase match {
				case "DATE" //1
				=> re("DATE")
				case "TEXT" | "CHARACTER" | "VARCHAR" | "TIME WITHOUT TIME ZONE" | "TIME WITH TIME ZONE" | "UUID" | "IP4" | "IP4RANGE" //8
				=> re("STRING")
				case "TIMESTAMP WITHOUT TIME ZONE" | "TIMESTAMP WITH TIME ZONE" //2
				=> re("TIMESTAMP")
				case "BOOL" | "BYTEA" | "BIGINT" | "SMALLINT" | "INTEGER" | "REAL" | "DOUBLE PRECISION" | "INTERVAL" | "BIT" | "VARBIT" | "NUMERIC" //11
				=> re("VALUE")
				case _ => re("VALUE")
			}
			// hive
			case "03" => sorc_field_type.toUpperCase match {
				case "DATE" => re("DATE")
				case "CHAR" | "STRING" | "VARCHAR" => re("STRING")
				case "TIMESTAMP" => re("STRING")
				case "DECIMAL" | "BINARY" | "TINYINT" | "FLOAT" | "BIGINT" | "DOUBLE" | "INT" | "SMALLINT" => re("VALUE")
				case "BOOLEAN" => re("VALUE")
				case _ => re("VALUE")
			}
			// postgresql
			case "04" => sorc_field_type.toUpperCase match {
				case "DATE" => re("TDATE")
				case "CHAR" | "VARCHAR" | "TEXT" => re("STRING")
				case "TIMESTAMP" => re("TIMESTAMP")
				case "SMALLINT"|"INTEGER"|"BIGINT"|"DECIMAL"|"NUMERIC"|"REAL"|"DOUBLE"|"SERIAL"|"BIGSERIAL" => re("VALUE")
				case "BOOLEAN" => re("STRING")
				case _ => re("VALUE")
			}
			// oracle
			case "05" => sorc_field_type.toUpperCase match {
					// 字符串类型
				case "CHAR"|"NCHAR"|"VARCHAR2"|"NVARCHAR2" => re("STRING")
					// 数字类型
				case "NUMBER"|"INTEGER"|"BINARY_FLOAT"|"BINARY_DOUBLE"|"FLOAT" => re("VALUE")
					// 日期类型
				case "DATE" => re("ODATE")
				case "TIMESTAMP" => re("OTIMESTAMP")
					// LOB
				case "CLOB"|"NCLOB"|"BLOB"|"BFILE"  => re("STRING")
					// RAW & LONG RAW类型
				case "LOONG"|"LONG RAW"|"RAW" => re("VALUE")
					// ROWID
				case "ROWID"|"UROWID" => re("STRING")
					//
				case _ => re("VALUE")
			}
			case _ => re("VALUE")

		}
	}

	/**
	  * 字段类型常量值包装
	  * @param sl
	  * @param tp
	  * @return
	  */
	def limitedField(sl: List[Any])(tp: String): String = {
		tp.toUpperCase match {
			case "DATE" => sl.mkString("cast('", "' as date), cast('", "' as date)")
			case "TIMESTAMP" => sl.mkString("cast('", "'as timestamp), cast('", "'as timestamp)")
			case "STRING" => sl.mkString(" '", "' ,'", "'")
			case "VALUE" => sl.mkString("", ",", "")
			case "TDATE" => {
				sl.map(i => SqlUtil.tdFormatDateConvert(i.toString)).mkString(" DATE '", "' , DATE '", "' ")
			}
			case "TTIMESTAMP" => sl.mkString(" TIMESTAMP '", "' , TIMESTAMP '", "' ")
			/**
			  * Add Oracle date  timestamp type
			  */
			case "ODATE" => sl.mkString(" to_date( '", "', 'yyyy-mm-dd') , to_date( '", "' , 'yyyy-mm-dd') ")
			case "OTIMESTAMP" => sl.mkString(" to_timestamp( '", "', 'yyyy-mm-dd hh24:mi:ss.ff') , to_timestamp( '", "' , 'yyyy-mm-dd hh24:mi:ss.ff')")
			case _ => sl.mkString("", ",", "")
		}
	}

	/*-------------------结果包装-------------------*/

	def resultSetToJson(columnNames: List[String], retSet: ResultSet, page_num: Int, page_size: Int, totalCount: Int): String = {
		val total_pages: Int = totalCount / page_size
		val totalProp = if (page_num == 1) s""""totalCount":$totalCount,""" else ""
		val pageSizeProp = if (page_num == 1) s""""page_size":$page_size,""" else ""
		val totalPagesProp = if (page_num == 1) s""""total_pages":$total_pages,""" else ""
		var retStr = s"""{$totalProp$pageSizeProp$totalPagesProp"dataLine":["""
		var r = 0
		while (retSet.next()) {
			val dataRow: mutable.LinkedHashMap[String, Any] = mutable.LinkedHashMap[String, Any]()
			for (i <- 1 to columnNames.length) {
				dataRow += (columnNames(i - 1) -> retSet.getString(i))
			}
			val comma = if (r == 0) "" else ","
			//println("rrr:" + r + " comma:" + comma)
			retStr = retStr + comma + Json.generate(dataRow)
			r += 1
		}

		retStr + """]}"""
	}

	def resultSetToTxt(columnNames: List[String], retSet: ResultSet, page_num: Int, page_size: Int, totalCount: Int): String = {
		val dataSplit: String = "\t"
		val rowSplit: String = "\n"
		val total_pages: Int = totalCount / page_size
		val totalProp = if (page_num == 1) s""""totalCount":$totalCount$dataSplit""" else ""
		val pageSizeProp = if (page_num == 1) s""""page_size":$page_size$dataSplit""" else ""
		val totalPagesProp = if (page_num == 1) s""""total_pages":$total_pages$dataSplit""" else ""
		var retStr = s"""$totalProp$pageSizeProp$totalPagesProp"""
		var r = 0
		while (retSet.next()) {
			for (i <- 1 to columnNames.length) {
				val dataASplit: String = if (i == 1) "" else dataSplit
				retStr = retStr + retSet.getString(i) + dataASplit
			}
			retStr = retStr + rowSplit
			r += 1
		}
		retStr
	}

	def resultSetToXml2(columnNames: List[String], retSet: ResultSet, encode: Encode.Value): String = {
		//var retStr = "<?xml version='1.0' encoding='" + encode.toString + "'?>\n"
		var retStr = "<?xml version='1.0' encoding='UTF-8'?>\n"
		retStr += "<ROWS>\n"
		//var rowid = 0
		while (retSet.next()) {
			var dataRow: String = "<ROW " //"<DATA ID=" + rowid.toString.mkString("\"","","\" ")
			for (i <- 1 to columnNames.length) {
				dataRow += columnNames(i - 1) + "=" + retSet.getString(i).toString.mkString("\"", "", "\" ")
			}
			dataRow += "/>\n"
			retStr += dataRow
			//rowid += 1
		}
		retStr += "</ROWS>"
		retStr
	}

	def resultSetToXml(columnNames: List[String], retSet: ResultSet, page_num: Int, page_size: Int, totalCount: Int): String = {
		//var retStr = "<?xml version='1.0' encoding='" + encode.toString + "'?>\n"
		val total_pages: Int = totalCount / page_size
		var retStr = "<?xml version='1.0' encoding='UTF-8'?>\n"
		var totalProps: String = ""
		if (page_num == 1) {
			totalProps = "totalCount=" + totalCount.toString.mkString("'", "", "'") + " page_size=" + page_size.toString.mkString("'", "", "'") + " total_pages=" + total_pages.toString.mkString("'", "", "'")
		}
		retStr += s"<lines $totalProps>\n"
		//var rowid = 0
		while (retSet.next()) {
			var dataRow: String = "<line>"
			for (i <- 1 to columnNames.length) {
				dataRow += "<" + columnNames(i - 1) + ">" + retSet.getString(i) + "</" + columnNames(i - 1) + ">"
			}
			dataRow += "</line>\n"
			retStr += dataRow
			//rowid += 1
		}
		retStr += "</lines>"
		retStr
	}

}


/**
  * ReadMe
  * modify record
  * 2017-09-05 10:15
  * 	context	modify function sourceCondition's parameter source_id -> source_type
  * 	detail		backup old  create new
  */
