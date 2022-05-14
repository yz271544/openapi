package com.teradata.openapi.framework.plugin

import java.sql.Statement

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.step.FetchStep
import com.teradata.openapi.framework.util.pluginUtil._

import scala.collection.{immutable, mutable}

/**
  * Created by hdfs on 2016/5/25.
  */
private[openapi] class FtchAst2Hdfs extends AsyncTaskPlugin[FetchStep] with  OpenApiLogging {
/*
	def test() = {
		val operCmd = OperCmd()
		operCmd.ftchT2Hdfs("abc", "def", "GBK", Encode.GBK)
	}
*/
	/**
	  * copy parameter
	  */
	var si:SourceInfoRow = _
	var schema:String = _

	/**
	  * Aster Execution of derivative preparation
	  */
	var dropTempTableSQL:String = _
	var createTempTableSQL:String = _

	/**
	  * data title context
	  */
	var titleContext:String = _

	/**
	  * derivative data cmd and derived table
	  */
	var processCmd:String = _
	var derivedTable:String = _

	var targetDir:String = _				//目标输出文件夹
	var targetPath:String = _ 				//目标文件名

	/**
	  * 生成导出SQL语句
	  * @param step
	  */
	def derivedTableSQL(step: FetchStep) {
		derivedTable = BaseUtil.getTableName(PluginEnv.plugFtchAstSchema,  step.retDataFinger, PluginEnv.plugTmpTableNamePrefix)
//		log.debug("driverTableSQL -> tempTableName :" + derivedTable)
		this.dropTempTableSQL = s"drop table if exists ${derivedTable}"
		log.debug("driverTableSQL -> tempTableName :" + dropTempTableSQL)
		this.titleContext = SqlUtil.projColumns(step.repArgs)
		log.debug("driverTableSQL -> titleContext :" + titleContext)
		for (elem <- step.reqArgs) {
			log.debug(elem.fieldName)
		}
		log.debug("+++++++++++++" + this.si.source_id)
		this.createTempTableSQL = "create fact table %s as (select %s from %s where 1=1 %s)".format(
				derivedTable,
				this.titleContext,
				step.tableInfo.tableFullName,
				SqlUtil.sourceCondition(this.si.source_id, this.si.source_type_code, step.reqArgs)
		)
		log.debug("Query SQL :" + this.createTempTableSQL)
	}

	override def init(step: FetchStep, template: String, env: Map[String, Any]): Unit = {
		log.info("FtchAst2Hdfs plugin is starting init...")
		this.si = step.source
		this.schema = si.deflt_schema
		this.derivedTableSQL(step)
//		this.targetDir = BaseUtil.getTargetPath(TPathType.NORMAL, step.retDataFinger)
		this.targetDir = BaseUtil.getName( step.retDataFinger, Some(TPathType.NORMAL), None, None)

		this.targetPath = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), Some(Encode.UTF8), None)
		val paraMap = this.assemble(this.si)
		log.debug("--------------------- :" + this.createTempTableSQL)
		this.processCmd = BaseUtil.replace(template, paraMap)
		log.debug("init -> processCmd :" + this.processCmd)

		log.debug( "this.targetDir = " + this.targetDir )
		log.debug( "this.targetPath = " + this.targetPath )
		log.debug( "this.processCmd = " + this.processCmd )
		log.debug( "this.dropTempTableSQL = " + this.dropTempTableSQL )
		log.debug( "this.title = " + this.titleContext)

		log.info("FtchAst2Hdfs plugin is end Initialization....")

	}

	/**
	  * 1 连接数据库
	  * 2 建临时表 并插入数据
	  * 3 执行导数命令 生成临时数据文件，并建临时数据文件导入到HDFS， 删除临时数据文件
	  * 4 finally drop临时表 断开连接
	  * @return	Int 0 成功 1 失败
	  */
	override def execute(): Int = {
		/**
		  * 	执行流程 ：
		  * 	1、创建临时表，并给临时表插数据
		  * 	2、执行导出脚本将数据导入到临时文件中
		  * 	3、创建HDFS文件路径
		  * 	4、put临时文件到目标文件中
		  * 	5、清理 ( 临时文件、删除临时表、断开数据库连接 )
		  */


		// 定义返回值
		var re = 0
		// 实例化执行类的对象
		val operCmd = OperCmd()
		// 建立连接连数据库
		val conn = SqlUtil.getConnection(this.si)
		val stat = conn.createStatement()

		try {
			log.debug( "this.targetDir = " + this.targetDir )
			log.debug( "this.targetPath = " + this.targetPath )
			log.debug( "this.processCmd = " + this.processCmd )
			log.debug( "this.dropTempTableSQL = " + this.dropTempTableSQL )
			log.debug( "this.title = " + this.titleContext)

			stat.executeUpdate(this.dropTempTableSQL)
			log.debug("drop temp table....")
			this.createTempTable(stat)
			log.debug("create temp table....")
			OperFileUtil.mkdir(this.targetDir, Some(PluginEnv.cacheFileSystem))
			log.debug("Create temp file path")
			// 执行导出语句并将导出结果加载到HDFS文件系统中,并删除临时存储文件
			re = operCmd.ftchA2Hdfs(this.targetPath,this.processCmd)

			if(re == 0) {
				this.setOut
				log.debug("Set out is finished")
			}else {
				log.debug("out is not set")
			}
			re

		}
		finally{
			stat.executeUpdate(this.dropTempTableSQL)
			log.debug("drop Temp table is end.")
			try{
				stat.close()
			}catch {
				case e:Exception	=> throw e
			}

			try {
				conn.close()
			} catch {
				case e:Exception	=> throw e
			}
		}
	}

	def createTempTable(stat:Statement)= {
		stat.executeUpdate(this.createTempTableSQL)
		log.debug("create Temp table is end  " )
	}



	def assemble(source: SourceInfoRow): mutable.Map[String,String] = {
		val paraMap = mutable.HashMap[String,String] ()
		paraMap += ("sourceip"		->	this.si.ip_addr)
		paraMap += ("database"		->	this.si.deflt_schema)
		paraMap += ("sourceuser"		->	this.si.user_name)
		paraMap += ("sourcepasswd"	->	this.si.pwd)
		paraMap += ("derivedTable"	->	this.derivedTable)
		paraMap += ("targetPath"		->	this.targetPath)
	}


	def setOut: Unit = {
		this.out += ("fileHdfsDir" 	-> this.targetDir)
		this.out += ("fileDir" 		-> this.targetDir)
		this.out += ("fileCode" 		-> PluginEnv.plugFtchFileCode)
		this.out += ("titleContext" 	-> this.titleContext)
		this.out += ("filedDelimiter" 	-> PluginEnv.plugFtchFieldDelimiter)
		this.out += ("fileRawLoc"		-> this.targetDir)
		log.debug("Out parameter")
	}

}

object	FtchAst2Hdfs extends App {
	val template = "ncluster_export -h ${sourceip} -d ${database} -U ${sourceuser} -w ${sourcepasswd} -D \"\\x01\" ${derivedTable} "
	val hs: Map[String, Any] = immutable.Map[String, Any]()
	val src: SourceInfoRow = new SourceInfoRow(1, "02", 2, "Ast", "192.168.20.90", 2, "beehive", "beehive", "beehive", 3, 3, 4, "com.asterdata.ncluster.Driver", true, true, true)

	val codename: Map[String, List[Int]] = immutable.Map("DEAL_DATE" -> List(201604))
	val tableInfo: TableInfo = new TableInfo("rptmart3.tb_rpt_bo_mon", codename, 1, true)


	val c1_a: SorcType = SorcType(2, "rptmart3", "tb_rpt_bo_mon", "tb_rpt_bo_mon", "xxx", "xxxx", 4, 0, 0)
	val lco1: List[SorcType] = List(c1_a)

	val reqArg1: ReqArg = new ReqArg("deal_date", "DECIMAL", lco1, 1, List(201604), ArgsNecy.NECESSARY.id, None)
	val repArg_DEAL_DATE: RepArg = new RepArg("DEAL_DATE", "DEAL_DATE", "INTEGER", "处理日期")
	val repArg_REGION_CODE: RepArg = new RepArg("REGION_CODE", "REGION_CODE", "CHAR", "地市代码")
	val repArg_CITY_CODE: RepArg = new RepArg("CITY_CODE", "CITY_CODE", "CHAR", "区县代码")
	val repArg_BARGAIN_NUM: RepArg = new RepArg("BARGAIN_NUM", "BARGAIN_NUM", "DECIMAL", "交易数")
	val repArg_SHOULD_PAY: RepArg = new RepArg("SHOULD_PAY", "SHOULD_PAY", "DECIMAL", "应收")
	val repArg_SHOULD_PAY_AMT: RepArg = new RepArg("SHOULD_PAY_AMT", "SHOULD_PAY_AMT", "DECIMAL", "应付金额")

	val repArgs: List[RepArg] = List(repArg_DEAL_DATE, repArg_REGION_CODE, repArg_CITY_CODE, repArg_BARGAIN_NUM, repArg_SHOULD_PAY, repArg_SHOULD_PAY_AMT)

	val ftchAst2Hdfs: FtchAst2Hdfs = new  FtchAst2Hdfs()
	val step: FetchStep = new FetchStep(src, tableInfo, List(reqArg1), repArgs, "test_td2hdfs_tb_rpt_bo_mon")
	ftchAst2Hdfs.init(step, template, hs)

	ftchAst2Hdfs.execute()

//	ftchAst2HdfsN.test()


}


