package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{ReqArg, SorcType, ArgsNecy, TableInfo}
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.pluginUtil._
import com.teradata.openapi.framework.step.CacheStep
import com.teradata.openapi.framework.util.UUIDUtils

import scala.collection.{immutable, mutable}
import scala.sys.process._
/**
  * Created by hdfs on 2016/7/14.
  */
private[openapi] class CacheTd2Hive extends AsyncTaskPlugin[CacheStep] with OpenApiLogging{
	var step: CacheStep = _

	val hvsInfo:SourceInfoRow = PluginEnv.plugCachHiveSourceInfo

	//column name sourceType targetType ppi_flag
	var selSql:String = _

	var tableColInfo:TableColumnInfo = _

	/**
	  * hive testTable  create table  create partition
	  */
	var hiveTableName:String = _
	var hiveTmpTableName:String = _

	var createHiveTableSQL:String = _
	var createHiveTmpTableSQL:String = _

	var dropHiveTmpTableSQL:String = _

	var processCmdTd:String = _
	var tableToParTableSQL:String = _

	override def init(step: CacheStep, template: String, env: Map[String, Any]): Unit = {
		log.info("CacheTd2Hive's init is start.")

		/**
		  * Copy parameter
		  */
		this.step = step

		/**
		  * generate hive two table's name
		  */

		this.hiveTableName = PluginEnv.plugCachSchema + "." + this.getTableName
		this.hiveTmpTableName = PluginEnv.plugCachTmpSchema + "." + this.getTableName + "_" + UUIDUtils.generateString(4)

		tableColInfo = this.getTableInfo

		println("tableColInfo context is ............")
		println(this.tableColInfo.getCycleName.toString)
		println(this.tableColInfo.getHiveNoParFields)
		println(this.tableColInfo.getHiveParFields)
		println(this.tableColInfo.getHiveParTitleContext)
		println(this.tableColInfo.getTitleContext)
		println(this.tableColInfo.getTargetParDate)
		println("meta data from sql: " + this.getSelPlString)

		val param = this.assemble(this.step.source, tableColInfo)
		val templateList =  BaseUtil.replace(template, param).split('@').toList

		println("*******templateList is" )
		templateList.foreach(println)

		this.processCmdTd = templateList(0)

		tableColInfo.isParTable match {
			case true => 	{
				this.createHiveTableSQL = PluginEnv.plugCachCreateParTableTemplate.format(
					this.hiveTableName, tableColInfo.getHiveParFields, tableColInfo.getPartitionContext
				)
				this.tableColInfo.getTargetParDate match {
					case true 	=>	{
						this.createHiveTmpTableSQL = PluginEnv.plugCachCreateTableTemplate.format(
							this.hiveTmpTableName, tableColInfo.getHiveNoParFields
						)
						this.dropHiveTmpTableSQL =  PluginEnv.plugDropTableTemplate.format(this.hiveTmpTableName)
						this.processCmdTd = templateList(0).format(this.hiveTmpTableName)
						this.tableToParTableSQL = tableColInfo.cycle.field_value.length match {
							case 1 =>	templateList(1)
							case _ =>	{
								val Array(head,tail)= templateList(2).split('#')
								head + tableColInfo.cycle.field_value.map(
									x=>tail.replace("${conditionMul}", tableColInfo.cycle.field_name + "=" + "'%s'".format(x))
								).mkString(" ")
							}
						}
					}
					case false 			=> 	this.processCmdTd = templateList(0).format(this.hiveTableName)
				}
			}
			case false =>	{
				this.createHiveTableSQL = PluginEnv.plugCachCreateTableTemplate.format(
					this.hiveTableName, tableColInfo.getPartitionContext
				)

				this.processCmdTd = templateList(0).format(this.hiveTableName)

			}

		}

		println("hiveTableName = " + this.hiveTableName)
		println("hiveTmpTableName = " + this.hiveTmpTableName)
		println("createHiveTableSQL = " + this.createHiveTableSQL)
		println("createHiveTmpTableSQL = " + this.createHiveTmpTableSQL)
		println("dropHiveTmpTableSQL = " + this.dropHiveTmpTableSQL)
		println("processCmdTd = " + this.processCmdTd)
		println("tableToParTableSQL = " + this.tableToParTableSQL)

		log.debug("createHiveTmpTableSQL :" + this.createHiveTmpTableSQL)
		log.debug("createHiveTableSQL :" + this.createHiveTableSQL)
		log.debug("dropHiveTmpTableSQL :" + this.dropHiveTmpTableSQL)
		log.debug("processCmdTd :" + this.processCmdTd)
		log.debug("tableToParTableSQL :" + this.tableToParTableSQL)


		log.info("CacheTd2Hive is init end.")
	}

	override def execute(): Int = {
		var re = 0
		val hiveConn = SqlUtil.getConnection(PluginEnv.plugCachHiveSourceInfo)
		val hiveStat = hiveConn.createStatement()
//		val tmpShellFilePath = PluginEnv.plugTmpCmdDir + PluginEnv.plugFileDelimiter + this.step.tableInfo.tableFullName + UUIDUtils.generateString(4) + ".sh"
		val tmpShellFilePath: String = BaseUtil.getName(
									BaseUtil.getAddSuffix(this.step.tableInfo.tableFullName, SuffixType.CMD, true),
									None,
									None,
									None
								)

		log.debug("Hive connection and statement is OK" + getHiveDropPartSQL)

		try{
			hiveStat.executeUpdate(this.createHiveTableSQL)

			this.getHiveDropPartSQL.foreach(hiveStat.executeUpdate(_))

//			FileUtil.writeFile(tmpShellFilePath, this.processCmdTd)
			/**
			  * local file system -> file
			  */
			OperFileUtil.writeFile(tmpShellFilePath, this.processCmdTd, None)
			this.tableColInfo.getTargetParDate match {
				case false => 	 "/bin/sh" #< new java.io.File(s"${tmpShellFilePath}") !
				case true => {
					hiveStat.executeUpdate(this.dropHiveTmpTableSQL)
					hiveStat.executeUpdate(this.createHiveTmpTableSQL)
					("/bin/sh" #< new java.io.File(s"${tmpShellFilePath}") !)
					hiveStat.executeUpdate(tableToParTableSQL)
				}
			}
			log.info("CacheTd2Hive's execute is end.")
			re
		}
		catch{
			case e:Exception => {
				re = 1
				log.info(e.getMessage)
				throw e
			}
		}
		finally{
//			if(!this.dropHiveTmpTableSQL.isEmpty)  hiveStat.executeUpdate(this.dropHiveTmpTableSQL)

//			FileUtil.removeFile(tmpShellFilePath)
			OperFileUtil.deleteFile(tmpShellFilePath, None)
			try{
				hiveStat.close()
			}catch {
				case e:Exception	=> throw e
			}

			try {
				hiveConn.close()
			} catch {
				case e:Exception	=> throw e
			}
		}
	}

	def getHiveDropPartSQL:List[String] = {
		var b:List[String] = null
		if(this.tableColInfo.isParTable) {
			b  = SqlUtil.getDropPartList(this.hiveTableName, this.tableColInfo.cycle, this.tableColInfo.cycle.field_value )
		}
		b
	}


	def getTableName = step.tableInfo.tableFullName.replace('.','_')

	def getSelPlString: String = SqlUtil.getSelPlString(this.step.tableInfo.tableFullName, 1)

	/**
	  * get data from postgres
	  * generate four Variable
	  * @ cycColumnInfo	:	ColumnInfo
	  * @ titleContext	:	String
	  * create table use
	  * @ fieldTargetInfo:	String
	  * @ partitionContext:	String
	  */
	def getTableInfo:TableColumnInfo= {
		println("getTableInfo is start")
		val conn = SqlUtil.getConnection(PluginEnv.plugCachPostgresSourceInfo)
		val stat = conn.createStatement()
		val mm = new mutable.HashMap[String, String]()
		var cycleColumnInfo:CycleInfo = null
		val re = stat.executeQuery(this.getSelPlString)

		while(re.next()) {
			val ff = re.getObject("ppi_flag").toString.toInt
			ff match {
				case 1 => {
					cycleColumnInfo = new CycleInfo(re.getObject("field_name").toString,
						re.getObject("sorc_field_type").toString,
						re.getObject("field_targt_type").toString,
						1,
						this.step.tableInfo.columnMap.get(re.getObject("field_name").toString).get
					)
				}
				case _ => mm.put(re.getObject("field_name").toString, re.getObject("field_targt_type").toString)
			}
		}
		stat.close()
		conn.close()

		new TableColumnInfo(cycleColumnInfo, mm)
	}

	def assemble(source: SourceInfoRow, tci:TableColumnInfo): mutable.Map[String,String] = {
		val paraMap = mutable.HashMap[String,String] ()
		paraMap += ("ip" 					-> 		this.step.source.ip_addr)
		paraMap += ("deflt_schema" 			-> 		this.step.source.deflt_schema)
		paraMap += ("user" 					-> 		this.step.source.user_name)
		paraMap += ("passwd" 				-> 		this.step.source.pwd)
		paraMap += ("fileFormat" 			-> 		PluginEnv.plugCacheHiveDefaultFileType)
		paraMap += ("method" 				->		PluginEnv.plugCacheTDCHSplitMethod )
		paraMap += ("sourceTable" 			-> 		this.step.tableInfo.tableFullName)
		paraMap += ("sourceFieldNames" 		-> 		this.tableColInfo.getTitleContext)
		paraMap += ("sourceCondition" 		-> 		SqlUtil.noAndCondition(this.step.source.source_type_code, this.tableColInfo.cycle))
		paraMap += ("targetDatabase" 		-> 		this.hvsInfo.deflt_schema)
		paraMap += ("targetTable" 			-> 		this.hiveTableName)
		paraMap += ("targetFieldNames" 		-> 		this.tableColInfo.getTitleContext)

		paraMap += ("hiveTableName" 		-> 		this.hiveTableName)
		paraMap += ("onlyCondition" 		-> 		(this.tableColInfo.cycle.field_name + "=" + "'%s'".format(this.tableColInfo.cycle.field_value.head.toString)).toString)
		paraMap += ("parTitleContext" 		-> 		this.tableColInfo.getPartitionContext)
		paraMap += ("hiveTmpTableName" 		-> 		this.hiveTmpTableName)
		paraMap
	}

}

object CacheTd2HiveMain extends App {
	println("------")
	println("------")
	val template = "hadoop jar $TDCH_JAR com.teradata.connector.common.tool.ConnectorImportTool  -libjars $LIB_JARS  -classname com.teradata.jdbc.TeraDriver -classname com.teradata.jdbc.TeraDriver -url jdbc:teradata://${ip}/CLIENT_CHARSET=EUC_CN,TMODE=TERA,CHARSET=ASCII,DATABASE=${deflt_schema},lob_support=off -username ${user} -password ${passwd} -jobtype hive -fileformat ${fileFormat} -method ${method} -sourcetable ${sourceTable} -sourcefieldnames \"${sourceFieldNames}\" -sourceconditions \"${sourceCondition}\" -targetdatabase ${targetDatabase} -targettable %s -targetfieldnames \"${targetFieldNames}\"@INSERT OVERWRITE TABLE  ${hiveTableName} PARTITION (${onlyCondition}) SELECT ${parTitleContext} FROM ${hiveTmpTableName}@FROM ${hiveTmpTableName} #INSERT OVERWRITE TABLE ${hiveTableName}  PARTITION (${conditionMul}) SELECT ${parTitleContext} WHERE ${conditionMul} "
	val hs = immutable.Map[String,Any]()

	val src = new SourceInfoRow(1, "td", 1,"td主仓库", "192.168.20.200", 9008,"dbc", "dtetl","dtetl2015", 1025,
		1,1,"com.teradata.jdbc.TeraDriver", true, true, true)
	val columnname = immutable.Map("DEAL_DATE"-> List("2016-03-08"))
	val c1_a = SorcType(1,"RPTMART3","TB_RPT_NEW_REGION_SALE_DAY", "T1", "DA","", 4,0,0)
	val lco1 = List(c1_a)
	val reqArg1 = new ReqArg("DEAL_DATE","DA",lco1,1,List("2016-03-08"), ArgsNecy.NECESSARY.id, None)
	val tableInfo = new TableInfo("RPTMART3.TB_RPT_NEW_REGION_SALE_DAY", columnname, 1, true)
	println("TestPlugin is Starting." + System.currentTimeMillis())
	val cacheTd2Hive = new CacheTd2Hive()
	cacheTd2Hive.init(new CacheStep(src,tableInfo), template, hs)
	/*cacheTd2Hive.execute()*/
}



/**
  * 流程
  * ********************************************
  * 0、init内容
  * 1、拼装hive查询表是否存在语句
  * 2、拼装hive是否存在分区语句
  * 3、拼装建表、建分区软件
  * 4、拼装导数语句
  * ********************************************
  * 0、excute内容
  * 1、查询是否hive中存在表
  * 2、查询是否hive中存在对应分区
  * 3、按照按照以上结果，生成对应的表或者对应的分区
  * 4、执行导数语句
  * *********************************************
  */


