package com.teradata.openapi.framework.plugin

import java.io.File

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.step.FetchStep
import com.teradata.openapi.framework.util.pluginUtil._

import scala.collection.mutable

/**
  * Created by hdfs on 2016/6/7.
  */
private[openapi] class FtchHive2Hdfs extends AsyncTaskPlugin[FetchStep] with OpenApiLogging {
	var targetDir:String = _
	var titleContext:String = _
	var targetPath:String = _
	var querySql:String = _
	var processCmd:String = _

	override def init(step: FetchStep, template: String, env: Map[String, Any]): Unit = {
//		this.targetDir = PluginEnv.plugFtchTargetPath + PluginEnv.plugFileDelimiter + step.retDataFinger
//		this.targetPath = this.targetDir + PluginEnv.plugFileDelimiter + step.retDataFinger + ".utf8"
		this.targetDir = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), None, None)
		this.querySql = this.driverTableSql(step)
		println("The hive query Sql is :" + this.querySql)
		log.debug("The hive query Sql is :" + this.querySql)
		log.debug("+++++reqArgs is " + step.reqArgs)

		this.processCmd = BaseUtil.replace(template, this.assemble)

		log.debug("processCmd is " + this.processCmd)
		println("processCmd is :" + this.processCmd)

	}

	override def execute(): Int = {
		var re = 0
		val hiveConn = SqlUtil.getConnection(PluginEnv.plugCachHiveSourceInfo)
		val hiveStat = hiveConn.createStatement()
		try{
			hiveStat.executeUpdate(this.processCmd)
			log.debug("Derived sql from hive is finished. context is : " + this.processCmd)
			re
		}
		catch {
			case e:Exception	=> {
				re =1
				throw e
			}
		}
		finally {
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

	def mkDir(dic:File) = {
		org.apache.commons.io.FileUtils.forceMkdir(dic)
	}

	def driverTableSql(step: FetchStep):String = {
		this.titleContext	= SqlUtil.projColumns(step.repArgs)
//		"select %s from %s where 1=1 %s".format(titleContext, tableCovHiveTableName(step.tableInfo.tableFullName), this.getCondition(step.source.source_id, step.reqArgs).toString)
		"select %s from %s where 1=1 %s".format(titleContext, tableCovHiveTableName(step.tableInfo.tableFullName), SqlUtil.sourceCondition(step.source.source_id, step.source.source_type_code,step.reqArgs).toString)
	}

	def assemble: mutable.Map[String,String] = {
		var paraMap = mutable.HashMap[String,String] ()
		paraMap += ("targetDir"			-> this.targetDir)
		paraMap += ("querySql"			-> this.querySql)
		paraMap
	}

	def getCondition(source_type_code: String, reqArgs: List[ReqArg]):String = {
		val s = TypeConvertUtil.reqCvtCondition(source_type_code)_
		println("s function is :" + s)
		var r:String = ""
		log.debug("---------------------source_id is :" + source_type_code)
		log.debug("---------------------reqList is :" + reqArgs)
		for (elem <- reqArgs) {
			val fun = s(elem.fieldValue)
			println("fun function is :" + fun)
			println("elem is " + elem)
			println("source_type_code is " + source_type_code)
			r += " and " + elem.fieldName + " in (" +  fun(elem.fieldTargtType) + " ) "
		}
		r
	}

	def tableCovHiveTableName(tablename:String ) :String ={
		PluginEnv.plugCachSchema + "." + tablename.replace(".", "_")
	}

	def setOut: Unit = {
		this.out += ("fileHdfsDir" 	-> this.targetDir)
		this.out += ("fileCode" 		-> PluginEnv.plugFtchFileCode)
		this.out += ("titleContext" 	-> this.titleContext)
		this.out += ("filedDelimiter" 	-> PluginEnv.plugFtchFieldDelimiter)
		this.out += ("fileRawLoc"		-> this.targetDir)
		this.out += ("fileRawCode"		-> PluginEnv.plugFtchFileCode)
		log.debug("Out parameter")
	}





}


object FtchHive2Hdfs extends App {
	val template = "hive -e \"insert overwrite directory '${targetDir}' ${querySql}\""
	val hs = Map[String,Any]()


	val columnname = Map("DEAL_DATE"-> List(201604))
	val c1_a: SorcType = SorcType(1, "rptmart3", "tb_rpt_bo_mon", "tb_rpt_bo_mon", "xxx", "xxxx", 4, 0, 0)

	val lco1 = List(c1_a)

	val reqArg1: ReqArg = new ReqArg("deal_date", "DECIMAL", lco1, 1, List(201604), ArgsNecy.NECESSARY.id, None)

	val repArg_DEAL_DATE: RepArg = new RepArg("DEAL_DATE", "DEAL_DATE", "INTEGER", "处理日期")
	val repArg_REGION_CODE: RepArg = new RepArg("REGION_CODE", "REGION_CODE", "CHAR", "地市代码")
	val repArg_CITY_CODE: RepArg = new RepArg("CITY_CODE", "CITY_CODE", "CHAR", "区县代码")
	val repArg_BARGAIN_NUM: RepArg = new RepArg("BARGAIN_NUM", "BARGAIN_NUM", "DECIMAL", "交易数")
	val repArg_SHOULD_PAY: RepArg = new RepArg("SHOULD_PAY", "SHOULD_PAY", "DECIMAL", "应收")
	val repArg_SHOULD_PAY_AMT: RepArg = new RepArg("SHOULD_PAY_AMT", "SHOULD_PAY_AMT", "DECIMAL", "应付金额")

	val tableInfo = new TableInfo("rptmart3.tb_rpt_bo_mon", columnname, 1, false)
	println("TestPlugin is Starting." + System.currentTimeMillis())
	val ftchHive2Hdfs = new FtchHive2Hdfs()
	ftchHive2Hdfs.init(new FetchStep(PluginEnv.plugCachHiveSourceInfo ,tableInfo,	List(reqArg1),	List(repArg_DEAL_DATE,repArg_REGION_CODE,repArg_CITY_CODE,repArg_BARGAIN_NUM,repArg_SHOULD_PAY,repArg_SHOULD_PAY_AMT),	"tb_aster_test"), template, hs)

}

/**
  * 流程
  * ********************************************
  * 0、init内容
  * 1、拼装hive查询语句
  * 2、拼装导数语句
  * ********************************************
  * 0、excute内容
  * 1、查询是路径是否存在，存在就删除
  * 2、执行导数语句
  * *********************************************
  */

