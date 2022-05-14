package com.teradata.openapi.framework.plugin

import java.io.File

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{ReqArg, SorcType, ArgsNecy, TableInfo}
import com.teradata.openapi.framework.util.pluginUtil.CycleInfo
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.pluginUtil._
import com.teradata.openapi.framework.step.CacheStep

import scala.collection.{immutable, mutable}
import scala.sys.process._
/**
  * Created by hdfs on 2016/7/14.
  */
private[openapi] class CacheAst2Hive extends AsyncTaskPlugin[CacheStep] with OpenApiLogging{

	val regex1 = "\\w+\\.[^_]+(.+)".r
	val regex2 = "\\w+\\.(.+)".r

	var step: CacheStep = _

	//column name sourceType targetType ppi_flag
	var selSql:String = _

	/**
	  * hive testTable  create table  create partition
	  */
	var astTmpTableName:String = _
	var hiveTableName:String = _
	var hiveTmpTableName:String = _

	var createAstTmpTableSQL:String = _
	var createHiveTableSQL:String = _
	var createHiveTmpTableSQL:String = _

	var dropAstTmpTableSQL:String = _
//	var dropHiveTableSQL:String = _
	var dropHiveTmpTableSQL:String = _

	var tempFilePath:String = _
	var tempHdfsFileDir:String = _
	var tempFileDir:String = _

	var processCmdAst:String = _
	var processCmdHive:String = _

	/**
	  * 流程
	  * ********************************************
	  * 0、init内容
	  * 1、生成导出表SQL语句（表存在删除，不存在建表）
	  * 2、执行建表语句
	  * 3、生成hive建表语句
	  * 4、生成hive建视图语句
	  * 5、生成导数语句
	  * ********************************************
	  * 0、excute内容
	  * 1、hive建表
	  * 2、执行建分区语句
	  * 3、执行导数过程
	  * 4、清理临时文件夹
	  * *********************************************
	  */
	override def init(step: CacheStep, template: String, env: Map[String, Any]): Unit = {
		log.info("CacheAst2Hive's init is start.")

		/**
		  * Copy parameter
		  */
		this.step = step

		/**
		  * generate hive two table's name
		  */

		val tmp_tableName = this.step.tableInfo.tableFullName match {
			case regex1(context) =>  PluginEnv.plugTmpTableNamePrefix + context + "_" + step.tableInfo.columnMap.values.head.mkString("_")
			case regex2(context) =>  PluginEnv.plugTmpTableNamePrefix + context + "_" + step.tableInfo.columnMap.values.head.mkString("_")
			case context@_ 		  =>  PluginEnv.plugTmpTableNamePrefix + context + "_" + step.tableInfo.columnMap.values.head.mkString("_")
		}

		this.astTmpTableName = PluginEnv.plugFtchAstSchema + PluginEnv.plugDBConnSign + tmp_tableName.substring(1, (tmp_tableName.length min PluginEnv.plugAstTableNameMax) )
		this.hiveTableName = PluginEnv.plugCachSchema + "." + this.getTableName
		this.hiveTmpTableName = PluginEnv.plugCachTmpSchema + "." + this.getTableName + "_" + step.tableInfo.columnMap.values.head.mkString("_")

		/**
		  * generate file dictionary and file path
		  */
		this.getTargetPath(hiveTmpTableName)

		val tableColInfo = this.getTableInfo

		this.createAstTmpTableSQL	=	PluginEnv.plugAstCreateTableAsTemplate.format(
			this.astTmpTableName, tableColInfo.getTitleContext, this.step.tableInfo.tableFullName
			, SqlUtil.condition(this.step.source.source_type_code, tableColInfo.cycle)
		)
		this.createHiveTmpTableSQL	=	PluginEnv.plugCachCreateExternalTableTemplate.format(
			this.hiveTmpTableName, tableColInfo.getHiveNoParFields, this.tempHdfsFileDir
		)
		this.createHiveTableSQL		=	tableColInfo.isParTable match {
			case true		=>	PluginEnv.plugCachCreateParTableTemplate.format(
				this.hiveTableName, tableColInfo.getHiveParFields, tableColInfo.getPartitionContext
			)
			case false 	=> 	PluginEnv.plugCachCreateTableTemplate.format(
				this.hiveTableName, tableColInfo.getHiveParFields
			)
		}

		this.dropAstTmpTableSQL = PluginEnv.plugDropTableTemplate.format(this.astTmpTableName)
		this.dropHiveTmpTableSQL =  PluginEnv.plugDropTableTemplate.format(this.hiveTmpTableName)
//		this.dropHiveTableSQL =  PluginEnv.plugDropTableTemplate.format(this.hiveTableName)
		/**
		  * generate parameter Map for replace template
		  */

		val param = this.assemble(this.step.source, tableColInfo)


		val templateList =  BaseUtil.replace(template, param).split('@').toList

		this.processCmdAst = templateList(0)

		log.debug("tableColInfo isn't partitiontable :"  + tableColInfo.isParTable)
		log.debug("Cycle value is :" + tableColInfo.cycle.field_value.size + tableColInfo.cycle)

		val templateInfo = 	(tableColInfo.isParTable, tableColInfo.cycle.field_value.size) match {
			case(false, _)		=> 	("NoPartition", templateList(1))
			case(true, 1 )		=>  ("OnePartition",templateList(2))
			case(true, _ )		=>  ("MulPartition",templateList(3))
		}

		log.debug("+++++++++++++++++++" + templateInfo)

		this.processCmdHive = this.getTableInfo.getCycleCondString(templateInfo._1, templateInfo._2, param)

		println("createAstTmpTableSQL :" + this.createAstTmpTableSQL)
		println("createHiveTmpTableSQL :" + this.createHiveTmpTableSQL)
		println("createHiveTableSQL :" + this.createHiveTableSQL)
		println("dropAstTmpTableSQL :" + this.dropAstTmpTableSQL)
		println("dropHiveTmpTableSQL :" + this.dropHiveTmpTableSQL)
		println("processCmdAst :" + this.processCmdAst)
		println("processCmdHive :" + this.processCmdHive)
		println("tempFilePath :" + this.tempFilePath)
		println("tempFileDir :" + this.tempFileDir)

		log.debug("createAstTmpTableSQL :" + this.createAstTmpTableSQL)
		log.debug("createHiveTmpTableSQL :" + this.createHiveTmpTableSQL)
		log.debug("createHiveTableSQL :" + this.createHiveTableSQL)
		log.debug("dropAstTmpTableSQL :" + this.dropAstTmpTableSQL)
		log.debug("dropHiveTmpTableSQL :" + this.dropHiveTmpTableSQL)
		log.debug("processCmdAst :" + this.processCmdAst)
		log.debug("processCmdHive :" + this.processCmdHive)
		log.debug("tempFilePath :" + this.tempFilePath)
		log.debug("tempFileDir :" + this.tempFileDir)

		log.info("CacheAst2Hive is init end.")
	}

	override def execute(): Int = {
		log.info("CacheAst2Hive's execute is start.")
		var re = 0
		val astConn = SqlUtil.getConnection(this.step.source)
		val astStat = astConn.createStatement()

		log.debug("Aster connection and statement is OK")

		val hiveConn = SqlUtil.getConnection(PluginEnv.plugCachHiveSourceInfo)
		val hiveStat = hiveConn.createStatement()

		log.debug("Hive connection and statement is OK")

		try{
			/**
			  * Aster temple table and export data
			  */
			astStat.executeUpdate(this.dropAstTmpTableSQL)
			astStat.executeUpdate(this.createAstTmpTableSQL)
			log.debug("CacheAst2Hive -> init -> ast create tmp table :" + this.createAstTmpTableSQL)

			this.mkDir(new File(this.tempFileDir))

			this.runExport

			log.debug("The Aster export file is ok." + this.tempFileDir)

			/**
			  * create external table in Hive
			  */
			hiveStat.executeUpdate(this.dropHiveTmpTableSQL)
			hiveStat.executeUpdate(this.createHiveTmpTableSQL)

			log.debug("The hive tmp external table is established.")
			log.debug("CacheAst2Hive -> init -> hive create tmp table :" + this.createHiveTmpTableSQL)

			/**
			  * create hive table and load data from hivetmptable to hivetable
			  */
			hiveStat.executeUpdate(this.createHiveTableSQL)
			log.debug("The hive tmp table is established.")
			hiveStat.executeUpdate(this.processCmdHive)
			log.debug("The hive load data is ok...")
			log.info("CacheAst2Hive's execute is end.")
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
			astStat.executeUpdate(this.dropAstTmpTableSQL)
			log.debug("CacheAst2Hive -> excute -> finally -> ast drop tmp table :" + this.dropAstTmpTableSQL)
			hiveStat.executeUpdate(this.dropHiveTmpTableSQL)
			log.debug("CacheAst2Hive -> excute -> finally -> hive drop tmp table :" + this.dropAstTmpTableSQL)
			this.dropDir(new File(this.tempFileDir))
			log.debug("CacheAst2Hive -> excute -> finally -> delete tmp file  :" + this.tempFileDir)
			try{
				astStat.close()
			}catch {
				case e:Exception	=> throw e
			}

			try{
				hiveStat.close()
			}catch {
				case e:Exception	=> throw e
			}

			try {
				astConn.close()
			} catch {
				case e:Exception	=> throw e
			}
			try {
				hiveConn.close()
			} catch {
				case e:Exception	=> throw e
			}
		}
	}

	def getTargetPath(tempFileName: String) = {
		tempFileDir = PluginEnv.plugTmpCachDir.endsWith(PluginEnv.plugFileDelimiter) match {
			case true 		=> PluginEnv.plugTmpCachDir + tempFileName
			case false		=> PluginEnv.plugTmpCachDir + PluginEnv.plugFileDelimiter + tempFileName
		}

		this.tempHdfsFileDir = BaseUtil.cvtFusepath(tempFileDir)
		this.tempFilePath = tempFileDir + PluginEnv.plugFileDelimiter + tempFileName
	}

	def mkDir(dic:File) = {
		org.apache.commons.io.FileUtils.forceMkdir(dic)
	}
	def dropDir(dic: File) = {
		if(dic.exists()){
			log.debug("CleanFs Directory [" + dic + "]")
			org.apache.commons.io.FileUtils.deleteDirectory(dic)
		}

	}


	def runExport :Int = {
		var re:Int = 0
		try {
			log.debug("this.processCmdAst is : " + this.processCmdAst)
			log.debug("this.tempFilePath is : " + this.tempFilePath)

//			val b = s"${processCmdAst}" #>  new java.io.File(s"${tempFilePath}") !
			re = s"${processCmdAst}" #>  new java.io.File(s"${tempFilePath}") !
		}
		catch {
			case e: Exception => re = 1;log.debug(e.printStackTrace().toString);throw e
		}
		log.debug("The runExport is end." + re)
		re
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
		paraMap += ("sourceip"			->	this.step.source.ip_addr)
		paraMap += ("database"			->	this.step.source.deflt_schema)
		paraMap += ("sourceuser"		->	this.step.source.user_name)
		paraMap += ("sourcepasswd"		->	this.step.source.pwd)
		paraMap += ("derivedTable"		->	this.astTmpTableName)
		paraMap += ("hiveTableName"		->	this.hiveTableName)
		paraMap += ("hiveTmpTableName"	->	this.hiveTmpTableName)
		paraMap += ("partitionName"		->	tci.getCycleName)
		paraMap += ("titleContext"		->	tci.getTitleContext)
		paraMap += ("parTitleContext"	->	tci.getParTitleContext)
	}

}



object CatchAst2HiveMain extends App {
	val template = "ncluster_export -h ${sourceip} -d ${database} -U ${sourceuser} -w ${sourcepasswd} -D \"\\x01\" ${derivedTable} " +
			"@INSERT OVERWRITE TABLE  ${hiveTableName} SELECT ${titleContext} FROM ${hiveTmpTableName}" +
			"@INSERT OVERWRITE TABLE  ${hiveTableName} PARTITION (${onlyCondition}) SELECT ${parTitleContext} FROM ${hiveTmpTableName}" +
			"@FROM ${hiveTmpTableName} \\n#INSERT OVERWRITE TABLE ${hiveTableName} \\nPARTITION (${conditionMul}) \\nSELECT ${parTitleContext} WHERE ${conditionMul}\\n"
	val hs = immutable.Map[String,Any]()

	val src:SourceInfoRow = new SourceInfoRow(2, "02", 2,"Aster", "192.168.20.90", 2406,"beehive", "beehive","beehive", 1025,
										1,1, "com.asterdata.ncluster.Driver", true, true, true)

	val columnname = immutable.Map("DEAL_DATE"-> List(201604))
//	val columnname = immutable.Map("DEAL_DATE"-> List(201604, 201606))
	val c1_a: SorcType = SorcType(2, "rptmart3", "tb_rpt_bo_mon", "tb_rpt_bo_mon", "xxx", "xxxx", 4, 0, 0)
	val lco1 = List(c1_a)
	val reqArg1: ReqArg = new ReqArg("deal_date", "DECIMAL", lco1, 1, List(201604), ArgsNecy.NECESSARY.id, None)
	val tableInfo = new TableInfo("rptmart3.tb_rpt_bo_mon", columnname, 1, true)
	println("TestPlugin is Starting." + System.currentTimeMillis())
	val cacheAst2Hive = new CacheAst2Hive()
	cacheAst2Hive.init(new CacheStep(src,tableInfo), template, hs)
}


