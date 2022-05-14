package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Encode
import com.teradata.openapi.framework.step.FetchStep
import com.teradata.openapi.framework.util.pluginUtil._

import scala.collection.mutable

/**
  * Created by hdfs on 2017/5/14.
  */
private[openapi] class FtchTd2Hdfs extends AsyncTaskPlugin[FetchStep] with OpenApiLogging{

	implicit def String2File(value: String) = new java.io.File(value)

	override def execute(): Int = {
		log.info( "Start execute..." )
		val operCmd = new OperCmd( )
		var re = 0
		try {
			log.info("Start export........")

			/**
				* runCmdFile 完成shell文件落地和执行过程
				* shell为命令参数，使用shell执行，原来还有使用VarFileExport工具执行的，参数传VarFileExport
				*/
			operCmd.runCmdFile( this.cmdFile, this.shContext, "shell" )
		} catch {
			case e : Error => {
				e.printStackTrace( )
				re = 1
				throw e
			}
		}finally {
			log.debug("FtchTd2Hdfs executor is ending.....")
		}
		this.setOut
		re
	}

	var TempParaMap = mutable.HashMap[String, String]() 	// 模板中需要的参数变量

	var titleContext:String = _ 			//数据Title
	var cmdFile:String = _						//shell 文件名
	/**
		* 执行内容
		*/
	var shContext:String = _ 						// 存储外部执行shell的内容
	var expFifoFile:String = _					// 命名管道名字，使用指纹来标识
	var tableName:String = _ 						// TD数据库要导出的表名
	/**
		* 目标文件路径，全路径与相对路径
		*/
	var	hdfsRelatExpDir:String = _ 				// HDFS 文件名	 相对 /
	var hdfsRelatExpFile:String = _ 			// HDFS 路径名 相对 /
	var hdfsExpFile:String = _ 					// HDFS 路径名	 full hdfs://
	var hdfsExpDir:String = _						// HDFS 文件名	 full hdfs://

	/**
		* 初始化各种参数
		* @param step
		* @param template
		* @param env
		* 文件夹路径
		* 导出SQL
		* 导入SQL
		*/
	override def init(step: FetchStep, template: String, env: Map[String, Any]): Unit = {
		// 相对路径，文件夹路径 保存无格式数据 具体格式为 /normal/指纹/
		this.hdfsRelatExpDir = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), None, None)
		// 相对路径，文件路径 保存无格式数据 具体格式为 /normal/指纹/指纹.GBK
		this.hdfsRelatExpFile = BaseUtil.getName(step.retDataFinger, Some(TPathType.NORMAL), Some(Encode.GBK), None)
		// 绝对路径，文件夹路径 保存无格式数据 具体格式为 HDFS://hdptd@SXMCC.COM/
		this.hdfsExpDir = BaseUtil.cvtFusepath(this.hdfsRelatExpDir)
		// 绝对路径，文件夹路径 保存无格式数据 具体格式为 HDFS://hdptd@SXMCC.COM/
		this.hdfsExpFile = BaseUtil.cvtFusepath(this.hdfsRelatExpFile)

		/**
			* 将路径信息加入到替换Map中
			*/
		this.TempParaMap += ("hdfsRelatExpDir" -> this.hdfsRelatExpDir)
		this.TempParaMap += ("hdfsRelatExpFile" -> this.hdfsRelatExpFile)
		this.TempParaMap += ("hdfsExpDir" -> this.hdfsExpDir)
		this.TempParaMap += ("hdfsExpFile" -> this.hdfsExpFile)



		/**
			* 连接TD库的参数
			*/
		this.TempParaMap += ("ftch_sourceip" -> step.source.ip_addr)
		this.TempParaMap += ("ftch_sourceport" -> step.source.port.toString)
		this.TempParaMap += ("ftch_sourceuser" -> step.source.user_name)
		this.TempParaMap += ("ftch_sourcepasswd" -> step.source.pwd)

		/**
			* 定义两个路径
			* 1、cmdFIle 执行外部shell文件路径 例如：/data/open_api/APP/test/70f9c357450471ff388810cbdbdac4f1.CMD
			* 2、命名管道问cmdFile 删除后缀		例如：/data/open_api/APP/test/70f9c357450471ff388810cbdbdac4f1
			*/
		this.cmdFile = BaseUtil.getName(step.retDataFinger, None, None, Some(SuffixType.CMD))
		this.TempParaMap += ("expFifoFile" -> BaseUtil.rmSuffix(this.cmdFile))

		/**
			*
			*/
		this.TempParaMap += ("tableName" -> step.tableInfo.tableFullName)
		this.TempParaMap += ("querySql" -> SqlUtil.getDriveSQL(step))
		this.TempParaMap += ("dataColumnDelimiter" -> PluginEnv.plugFieldRawDelimiter)
		this.TempParaMap += ("tdSession" -> "2")
		// 使用指纹作为临时文件名， 具体路径为/cache
		this.shContext = BaseUtil.replace(template, this.TempParaMap)
		//////////////////////////////////////////////////////////////////////////


		log.info("Shell file Name is : " + this.cmdFile)
		this.titleContext = SqlUtil.projColumns(step.repArgs)
		log.info("shell file context is : " + this.titleContext)
	}

	def setOut: Unit = {
		this.out += ("fileHdfsDir" -> this.hdfsExpDir)
		this.out += ("fileDir" -> this.hdfsRelatExpDir)
		this.out += ("fileCode" -> PluginEnv.plugFtchTdFileCode)
		this.out += ("titleContext" -> this.titleContext)
		this.out += ("filedDelimiter" -> PluginEnv.plugFtchFieldDelimiter)
		this.out += ("fileRawLoc" -> this.hdfsRelatExpFile)
		this.out += ("fileRawCode" -> PluginEnv.plugFtchFileCode)
		log.debug( "Out parameter" )

	}
}

object	FtchTd2Hdfs extends App {



	/*val template = "LOGON ${ftch_sourceip}/${ftch_sourceuser},${ftch_sourcepasswd}\nSESSIONS 2\nOUTFILE ${targetPath}\nDELIMITER \"\u0001\"\nTD_CHARSET ASCII \nQUERY_BAND \"JOB=${jobName};\"\n--SQL TEXT --\n${querySql}"
	val hs: Map[String, Any] = immutable.Map[String, Any]()
	val src: SourceInfoRow = new SourceInfoRow(1, "01", 1, "TD", "192.168.20.200", 2, "dtetl", "dtetl", "dtetl2015", 2, 1, 2, "com.teradata.jdbc.TeraDriver", true, true, true)

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

	val ftchTd2HdfsN: FtchTd2Hdfs = new  FtchTd2Hdfs()
	val step: FetchStep = new FetchStep(src, tableInfo, List(reqArg1), repArgs, "test_td2hdfs_tb_rpt_bo_mon")
	ftchTd2HdfsN.init(step, template, hs)

	("querySql's value is  " + ftchTd2HdfsN.querySql)
	println("cmdContext's value is  " + ftchTd2HdfsN.cmdContext)
	println("titleContext's value is  " + ftchTd2HdfsN.titleContext)
	println("targetFtchPath's value is  " + ftchTd2HdfsN.targetFtchPath)
	println("cmdFile's value is  " + ftchTd2HdfsN.cmdFile)

	val m: mutable.HashMap[String, String] = BaseUtil.splitDicAndPath( ftchTd2HdfsN.targetFtchPath )

	println("targetFtchPath's value is  " + ftchTd2HdfsN.targetFtchPath)
	println( "m = " + m )

	println( BaseUtil.getName("", Some(TPathType.JSON), None, None) )
	println( BaseUtil.getName("", None, None, None) )*/
}



