package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Encode
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.step.FetchStep
import com.teradata.openapi.framework.util.pluginUtil.{BaseUtil, PluginEnv, SqlUtil, TPathType}

import scala.collection.mutable
import scala.sys.process._

/**
  * Created by hdfs on 2016/5/12.
  */
private[openapi] class FtchOra2Hdfs extends AsyncTaskPlugin[FetchStep] with OpenApiLogging{



	var targetDir:String = _				//目标输出文件夹
	var querySql:String = _					//查询SQL
	var processCmd:String = _				//执行导数命令
	//var cmdFile:String = _					//导数工具输入文件名
	var cmdContext:String = _				//导数工具输入文件内容
	var targetPath:String = _ 				//输出文件名
	var titleContext:String = _ 			//数据title


	def driverTableSql(step: FetchStep):String = {
		val sourceTableName = step.tableInfo.tableFullName
		this.titleContext	= SqlUtil.projColumns(step.repArgs)
		"select %s from %s where 1=1 %s".format(titleContext, sourceTableName, SqlUtil.sourceCondition(step.source.source_id, step.source.source_type_code, step.reqArgs).toString)
	}

	override def init(step: FetchStep, template: String, env: Map[String, Any]): Unit = {
		log.info("Start init")
//		this.targetDir = this.generateTargetDir(PluginEnv.plugFtchTargetPath, step.retDataFinger)
		this.targetDir = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), None, None)
		log.debug("TargetDir is " + this.targetDir)
		this.querySql = this.driverTableSql(step)
		log.debug("Generate derived SQL")
//		this.targetPath = "%s%s%s".format(this.targetDir, PluginEnv.plugFileDelimiter, generateDataFileName(step.retDataFinger))
		this.targetPath = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), Some(Encode.GBK), None)
		this.cmdContext = BaseUtil.replace(template, this.assemble(step.source, step.retDataFinger))
		log.debug("Generate cmd file context. -> " + this.cmdContext)
		log.info("End init")

	}

	override def execute(): Int = {
		log.info("Start execute...")
		var re = 0
		try {
			re = this.run
			log.debug("Excute Fetch cmd and return value is " + re)
			this.setOut
			log.debug("Set out parameter...")
			re
		}
		catch {
			case e:RuntimeException 		=>	{
				re =1
				log.debug("RuntimeException: " + e.getStackTrace)
				throw e
			}
			case e:NullPointerException 	=>	{
				re = 1
				log.debug("NullPointerException: " + e.getStackTrace)
				throw e
			}
			case e:Exception 				=>	{
				re = 1
				log.debug("Exception: " + e.getStackTrace)
				throw e
			}
		}
		finally {
			log.debug("Excute finally..")
		}
	}

	def assemble(source: SourceInfoRow, retDataFinger:String): mutable.Map[String,String] = {
		var paraMap = mutable.HashMap[String,String] ()
		paraMap += ("connect" 	-> s"jdbc:oracle:thin:@$source.ip_addr:$source.port:$source.deflt_schema")
		paraMap += ("username"		-> source.user_name)
		paraMap += ("password"		-> source.pwd)
		paraMap += ("outpath"			-> this.targetPath)
		paraMap += ("query"				-> this.querySql)
		paraMap += ("encode"				-> Encode.GBK.toString)
		paraMap += ("delimiter"				-> PluginEnv.plugFieldDelimiter)
		paraMap
	}

	def run :Int = {
		var re:Int = 0
		log.debug("cmdContext is " + this.cmdContext)
		try
			re = this.cmdContext !
		catch {
			case ex: Exception => println("The hadoop's program is error.")
		}
		log.debug("The run is end." + re)
		re
	}


	def setOut: Unit = {
		this.out += ("fileHdfsDir" -> this.targetDir)
		this.out += ("fileDir" -> this.targetDir)
		this.out += ("fileCode" -> PluginEnv.plugFtchTdFileCode)
		this.out += ("titleContext" -> this.titleContext)
		this.out += ("filedDelimiter" -> PluginEnv.plugFtchFieldDelimiter)
		this.out += ("fileRawLoc"		-> this.targetDir)
		this.out += ("fileRawCode"		-> PluginEnv.plugFtchFileCode)
		log.debug("Out parameter")
	}


}
