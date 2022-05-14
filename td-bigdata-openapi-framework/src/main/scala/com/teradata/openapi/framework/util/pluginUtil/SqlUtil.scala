package com.teradata.openapi.framework.util.pluginUtil

import java.sql.{Statement, ResultSet, DriverManager, Connection}

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{RepArg, ReqArg}
import com.teradata.openapi.framework.expression.ExpressionParser
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.step.{CacheStep, FetchStep}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.util.matching.Regex.Match

/**
  * Created by hdfs on 2017/5/16.
  */
private[openapi] object SqlUtil extends OpenApiLogging {

	val tdDateFmtPattern = new Regex( """(\d{4})(\d{2})(\d{2})""", "year", "month", "day" )

	def tdFormatDateConvert(text: String): String = tdDateFmtPattern replaceAllIn(text, (m: Match) =>
		"%s-%s-%s" format(m group "year", m group "month", m group "day")
			)


/*	---- 同名函数删除 by hdfs 2017/8/31 16:10
	def limitedField(sl: List[Any])(tp: String): String = {
		tp match {
			case "date" => sl.mkString("cast('", "' as date), cast('", "' as date)")
			case "timestamp" => sl.mkString("cast('", "'as timestamp), cast('", "'as timestamp)")
			case "string" => sl.mkString(" '", "','", "'")
			case "value" => sl.mkString("", ",", "")
			case "Tdate" => {
				sl.map(i => tdFormatDateConvert(i.toString)).mkString(" DATE '", "' , DATE '", "' ")
			}
			case _ => sl.mkString("", ",", "")
		}
	}*/


	/**
	  * Get SQL Connection
	  * param 		SourceInfoRow
	  * return		Connection
	  *
	  */
	def getConnection(si: SourceInfoRow): Connection = {
		val db: String = si.source_type_code match {
			case "01" => "teradata"
			case "02" => "aster"
			case "03" => "hive"
			case "04" => "postgresql"
			case "05" => "oracle"
			case _ => "noMatch"
		}
		val ip: String = si.ip_addr
		val port: String = si.port.toString
		val user: String = si.user_name
		val passwd: String = si.pwd
		val databasename: String = si.deflt_schema

		println("************" + db)

		val conn_str = db match {
			case "teradata" => {
				//				Class.forName("com.teradata.jdbc.TeraDriver")
				Class.forName(si.drv_name)
				s"jdbc:${db}://${ip}/DBS_PORT=${port},TMODE=TERA,CHARSET=ASCII,CLIENT_CHARSET=GBK,DATABASE=${databasename}"
			}
			case "aster" => {
				//println("--++--" + s"jdbc:ncluster://${ip}:${port}/${databasename}" + " drvName:" +  si.drv_name)
				//Class.forName("com.asterdata.ncluster.Driver")
				Class.forName(si.drv_name)
				s"jdbc:ncluster://${ip}:${port}/${databasename}"
			}
			case "hive" => {
				//				Class.forName("org.apache.hive.jdbc.HiveDriver")
				Class.forName(si.drv_name)
				s"jdbc:${db}2://${ip}:${port}/${databasename}"

			}
			case "postgresql" => {
				//				Class.forName("org.postgresql.Driver")
				Class.forName(si.drv_name)
				s"jdbc:${db}://${ip}:${port}/${databasename}"
			}
			case "oracle" =>
				Class.forName(si.drv_name)
				s"jdbc:${db}:thin:@${ip}:${port}:${databasename}"
			case _ => {
				println("The database URL can't find.")
				"Can't matching"
			}
		}

		println("Connection String." + conn_str.toString)

		DriverManager.getConnection(conn_str.toString, user, passwd)
	}


	/**
	  * Close ResultSet context
	  * @param rst
	  */
	def closeResultSet(rst: ResultSet) = {
		if (rst != null) {
			try {
				rst.close()
			}
			catch {
				case e: Exception => throw e
			}
		}
	}

	/**
	  * Close Statement
	  * @param stat
	  */
	def closeStatement(stat: Statement) = {
		if (stat != null) {
			try {
				stat.close()
			} catch {
				case e: Exception => throw e
			}
		}
	}

	/**
	  * close Connection
	  * @param conn
	  */
	def closeConn(conn: Connection) = {
		if (conn != null) {
			try {
				conn.close()
			} catch {
				case e: Exception => throw e
			}
		}
	}

	/**
	  * Close Result Statement conn ALL
	  * @param conn
	  * @param stat
	  * @param rst
	  */
	def close(conn: Connection, stat: Statement, rst: ResultSet) = {
		try {
			closeResultSet(rst)
		} catch {
			case e: Exception => e.printStackTrace()
		} finally {
			try {
				closeStatement(stat)
			} catch {
				case e: Exception => e.printStackTrace()
			} finally {
				closeConn(conn)
			}
		}
	}

/*	/**
	  * source_id不合适应该用source_type 注释 时间 2017-09-05 10:09
	  * Cycle get Sql condition
	  * @param source_id
	  * @param reqList
	  * @return
	  */
	def sourceCondition(source_id: Int, reqList: List[ReqArg]): String = {
		val s = TypeConvertUtil.reqCvtCondition(source_id) _
		//println("s function is :" + s)
		var r: String = ""
		log.debug("---------------------source_id is :" + source_id)
		log.debug("---------------------reqList is :" + reqList)
		for (elem <- reqList) {
			val fun = s(elem.fieldValue)
			/*println("fun function is :" + fun)
			println("elem is " + elem)
			println("source_id is " + source_id)*/
			val c: List[String] = elem.field_sorc_type.map(TypeConvertUtil.getSorcTypeType(source_id, _))
			r += " and " + elem.fieldName + " in " + fun(c.head.toString)
		}
		r
		for (elem <- reqList) {
			val fun = s(elem.fieldValue)
			/*println("fun function is :" + fun)
			println("elem is " + elem)
			println("source_id is " + source_id)*/
			if (elem.fieldName eq "_expression_"){
				ExpressionParser.expressionParser(elem.fieldValue.head.toString,source_id,elem.expressionAtomSorcTypeMap.get)
			} else {
				val c: List[String] = elem.field_sorc_type.map(TypeConvertUtil.getSorcTypeType(source_id, _))
				r += " and " + elem.fieldName + " in (" + fun(c.head.toString) + ")"
			}
		}
		r
	}*/


	/**
	  * 改为用source_type来判断条件 修改时间 2017-09-05 10:11
	  * Cycle get Sql condition
	  * @param reqList
	  * @return
	  */
	def sourceCondition(source_id:Int, stp: String, reqList: List[ReqArg]): String = {
		val s = TypeConvertUtil.reqCvtCondition(stp) _
		//println("s function is :" + s)
		var r: String = ""
		log.debug("---------------------source_id is :" + stp)
		log.debug("---------------------reqList is :" + reqList)

		for (elem <- reqList) {
			val fun = s(elem.fieldValue)
			/*println("fun function is :" + fun)
			println("elem is " + elem)
			println("source_id is " + source_id)*/
			log.debug("The field name is " + elem.fieldName)
			if ("_EXPRESSION_" equals elem.fieldName.toUpperCase){
				log.debug("expression is used.......====....")
				r += " and " + ExpressionParser.expressionParser(elem.fieldValue.head.toString,stp,elem.expressionAtomSorcTypeMap.get)
			} else {
				val c: List[String] = elem.field_sorc_type.map(TypeConvertUtil.getSorcTypeType(source_id, _))
				r += " and " + elem.fieldName + " in (" + fun(c.head.toString) + ")"
			}
		}
		r
	}


	def condition(source_type_code: String, cycleInfo: CycleInfo): String = {
		val s = TypeConvertUtil.reqCvtCondition(source_type_code) _
		//println("s function is :" + s)
		val fun = s(cycleInfo.field_value)
		" and " + cycleInfo.field_name + " in (" + fun(cycleInfo.sorc_field_type) + ')'
	}

	def noAndCondition(source_type_code: String, cycleInfo: CycleInfo): String = {
		val s = TypeConvertUtil.reqCvtCondition(source_type_code) _
		//println("s function is :" + s)
		val fun = s(cycleInfo.field_value)
		cycleInfo.field_name + " in (" + fun(cycleInfo.sorc_field_type) + ')'
	}

	def projColumns(repList: List[RepArg]): String = {
		val res = for (elem <- repList) yield elem.fieldName
		res.mkString(",") 						//res.sorted.mkString(",")
	}

	def getDriveSQL(step: FetchStep)= "select %s from %s where 1=1 %s".format(
		this.projColumns(step.repArgs)
		, step.tableInfo.tableFullName
		, this.sourceCondition(step.source.source_id, step.source.source_type_code, step.reqArgs).toString
	)


	def getSelPlString(tableFullName: String, sourceid: Int): String = {
		println(tableFullName)
		val Array(schema: String, table: String) = tableFullName.toUpperCase.split('.')
		//		"select \n field_name, sorc_field_type, field_targt_type, ppi_flag \n from opi.vw_sorc_field_info \n where source_id = %d \n and schema_name = '%s'\n and tab_name = '%s'".format(sourceid, schema, table)
		PluginEnv.plugCachPlugColumnInfoQuery.format(sourceid, schema, table)
	}

	def getCreatePartList(tableFullName: String, cycleCycleInfo: CycleInfo, partContextList: List[Any]): List[String] = {
		println("partContextList :" + partContextList)
		/*	val f = for (elem <- partContextList) {
			yield PluginEnv.plugCacheDropPartTemplate.format(tableFullName, elem.toString) + ";" + PluginEnv.plugCacheCreatePartTemplate.format(tableFullName, elem.toString)
		  }*/
		val g = hivePartition(cycleCycleInfo.field_name)(cycleCycleInfo.field_targt_type) _

		val f = partContextList.map(p => PluginEnv.plugCachCreateParTableTemplate.format(tableFullName, g(p)))
		//		val f = partContextList.map(g(_)).map(p =>  PluginEnv.plugCacheDropPartTemplate.format(tableFullName, p))
		//		val f = partContextList.map(g(_)).map(p =>  PluginEnv.plugCacheDropPartTemplate.format(tableFullName, p) + ";" + PluginEnv.plugCacheCreatePartTemplate.format(tableFullName, p) )
		//		map((p:String) =>  PluginEnv.plugCacheDropPartTemplate.format(tableFullName, g(p).toString) +  PluginEnv.plugCacheCreatePartTemplate.format(tableFullName, g(p).toString) )
		println(f)
		f
	}

	def getDropPartList(tableFullName: String, cycleCycleInfo: CycleInfo, partContextList: List[Any]): List[String] = {
		println("partContextList :" + partContextList)
		/*	val f = for (elem <- partContextList) {
			yield PluginEnv.plugCacheDropPartTemplate.format(tableFullName, elem.toString) + ";" + PluginEnv.plugCacheCreatePartTemplate.format(tableFullName, elem.toString)
		  }*/
		val g = hivePartition(cycleCycleInfo.field_name)(cycleCycleInfo.field_targt_type) _

		val f = partContextList.map(p => PluginEnv.plugCachDropPartitionTemplate.format(tableFullName, g(p)))
		println(f)
		f
	}

	def genPartitionDDL(step: CacheStep): List[String] = {
		var partitionString = ""

		val template = "alter table %s ADD IF NOT EXISTS PARTITION  (%s)".format(step.tableInfo.tableFullName, step.tableInfo.tableFullName)

		new ListBuffer[String]().toList
	}


	def getColumnsString(source: SourceInfoRow)(tableName: String): String = {
		val conn = getConnection(source)
		val stat = conn.createStatement()

		val Array(schema_name: String, tab_name: String) = tableName.toUpperCase.split(".")
		val sql = "select field_name, field_targt_type, ppi_flag from opi.vw_sorc_field_info where source_id = %s and schema_name = '%s' and tab_name = '%s'".format(source.source_id, schema_name, tab_name)

		var result: ResultSet = null
		try {
			result = stat.executeQuery(sql)
		} catch {
			case e: Exception => throw e
		} finally {
			this.closeStatement(stat)
			this.closeConn(conn)
		}

		result.toString

	}

	/**
	  * 生成建表语句
	  *
	  * @param step
	  * @return
	  */
	def genHiveDDL(step: CacheStep): String = {
		val tableHead = "create table if not exist $tableName "
		//列分隔符为01空格，行分割符为换行，按照行存储，存储格式为ORCFILE
		val tableTail = "ROW FORMAT DELIMITED \nFIELDS TERMINATED BY '\u0001' \n LINES TERMINATED BY ‘\\n’\nSTORED AS ORCFILE"

		val conn = this.getConnection(step.source)
		val stat = conn.createStatement()
		val Array(schema_name: String, tab_name: String) = step.tableInfo.tableFullName.toUpperCase.split(".")
		val sql = "select field_name, field_targt_type, ppi_flag from opi.vw_sorc_field_info where source_id = %s and schema_name = '%s' and tab_name = '%s'".format(step.source.source_id, schema_name, tab_name)
		val re = new ListBuffer[String]()
		val partitionString = new ListBuffer[String]()

		try {
			val rs = stat.executeQuery(sql)
			while (rs.next()) {
				val temp = rs.getString("field_name") + "  " + rs.getString("field_targt_type")
				if (rs.getString("ppi_flag").equals("0")) partitionString += temp
				re += temp
			}
		} catch {
			case e: Exception => throw e
		} finally {
			this.closeStatement(stat)
			this.closeConn(conn)
		}
		var tableDDL = ""

		if (partitionString.isEmpty) {
			tableDDL = tableHead + "(" + re.mkString(", ") + ") \n" + tableTail
		} else {
			tableDDL = tableHead + "(" + re.mkString(", ") + ") \n partitioned by (" + partitionString.mkString(" ,") + ")" + tableTail
		}

		tableDDL
	}

	def partition(fieldName: String, fieldType: String, fieldValue: String): String = {
		fieldType match {
			case "TINYINT" => "$name=${value}"
			case "SMALLINT" => "$name=${value}"
			case "INT" => "$name=${value}"
			case "BIGINT" => "$name=${value}"
			case "BOOLEAN" => "$name=${value}"
			case "FLOAT" => "$name=${value}"
			case "DOUBLE" => "$name=${value}"
			case "STRING" => "$name=${value}"
			case "BINARY" => "$name=${value}"
			case "TIMESTAMP" => "$name=${value}"
			case "DECIMAL" => "$name=${value}"
			case "CHAR" => "$name=${value}"
			case "VARCHAR" => "$name=${value}"
			case "DATE" => "$name=${value}"
		}
		"ok "
	}

	def hivePartition(columnName: String)(columnType: String)(columnValue: Any): String = {
		val value = columnValue.toString
		columnType match {
			case "DATE" => columnName + "=" + s"'${value}'"
			case "TIMESTAMP" => columnName + "=" + s"'${value}'"
			case "CHAR" | "STRING" | "VARCHAR" => columnName + "=" + s"'${value}'"
			case "DECIMAL" | "BINARY" | "TINYINT" | "FLOAT" | "BIGINT" | "DOUBLE" | "INT" | "SMALLINT" => columnName + "=" + value
			case "BOOLEAN" => columnName + "=" + value
			case _ => columnName + "=" + value
		}
	}

}


/**
  * 周期信息 齐伟 ADD 2017/5/18 18:23:10
  *
  * @param field_name
  * @param sorc_field_type
  * @param field_targt_type
  * @param ppi_flag
  * @param field_value
  */
case class CycleInfo(field_name: String, sorc_field_type: String, field_targt_type: String, ppi_flag: Int, field_value: List[Any])

/**
  * ReadMe
  * modify record
  * 2017-09-05 10:12
  * 	context	modify function sourceCondition's parameter source_id -> source_type
  * 	detail		backup old  create new
  */
