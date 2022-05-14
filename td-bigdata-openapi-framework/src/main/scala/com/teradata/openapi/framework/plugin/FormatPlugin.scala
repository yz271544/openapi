package com.teradata.openapi.framework.plugin

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Format
import com.teradata.openapi.framework.step.FormatStep
import com.teradata.openapi.framework.util.pluginUtil._

import scala.collection.mutable

/**
  * Created by hdfs on 2016/6/2.
  * 输入编码格式，输出编码格式，输入分隔符，输出分割符，输入文件路径，输出文件路径，输入文件格式，封装格式，title内容
  */
private[openapi] class FormatPlugin extends AsyncTaskPlugin[FormatStep] with OpenApiLogging {
	/**
	  * 得到文件输出路径目录
	  * @param retFormatFinger
	  * @return
	  */
	def getOutDir(retFormatFinger: String):String = {
/*		if (PluginEnv.plugFrmtTargetPath.endsWith(PluginEnv.plugFileDelimiter)) {
			PluginEnv.plugFrmtTargetPath + this.formatType + PluginEnv.plugFileDelimiter + retFormatFinger
		}else {
			PluginEnv.plugFrmtTargetPath + PluginEnv.plugFileDelimiter + this.formatType + PluginEnv.plugFileDelimiter + retFormatFinger
		}*/
		BaseUtil.getName(retFormatFinger, Some(BaseUtil.getTPathType(this.formatType)),None, None)
	}

	/**
	  * 得到文件输出路径目录
	  * @param retFormatFinger
	  * @return
	  */
	def getOutHdfsDir(retFormatFinger: String): String = {
/*		if(PluginEnv.plugFrmtTargetPath.endsWith(PluginEnv.plugFileDelimiter)){
			PluginEnv.plugFrmtTargetPath + this.formatType + PluginEnv.plugFileDelimiter + retFormatFinger
		}else {
			PluginEnv.plugFrmtTargetPath + PluginEnv.plugFileDelimiter + this.formatType + PluginEnv.plugFileDelimiter + retFormatFinger
		}*/
		BaseUtil.getName(retFormatFinger, Some(BaseUtil.getTPathType(this.formatType)), None, None)
	}

	/**
	  * 得到文件输入路径
	  * @param retDataFinger
	  * @return
	  */
	def getInHdfsDir(retDataFinger: String): String = {
/*		log.debug("PluginEnv.plugFrmtSourcecHdfsPath" + PluginEnv.plugFrmtSourcePath)
		if(PluginEnv.plugFrmtSourcePath.endsWith(PluginEnv.plugFileDelimiter)){
			PluginEnv.plugFrmtSourcePath + retDataFinger
		}else {
			PluginEnv.plugFrmtSourcePath + PluginEnv.plugFileDelimiter + retDataFinger
		}*/
		BaseUtil.getName(retDataFinger, Some(TPathType.NORMAL), None, None)
	}

	/**
	  * static variable
	  * 空格、路径分割符
	  */
	//	val template = s"hadoop jar ${jarPath} ${inPath} ${inCode} ${inFieldDelimiter} ${outPath} ${outCode} ${outFieldDelimiter} ${formatType} ${titleString}"

	var inHdfsDir:String = _
	var outDir:String = _
	var outHdfsDir:String = _
	var inCode:String = _
	var outCode:String = _
	var inFieldDelimiter:String = _
	var outFieldDelimiter:String = _
	var formatType:String = _
	var titleContext:String = _

	var processCmd :String = _

	var checkFileName:String = _
	var titleFileName:String = _
	var format:Format = _



	/**
	  * 1、生成cmd命令
	  * 2、生成title文件参数
	  * 3、生成校验文件参数
	  * @param step
	  * @param template
	  * @param env
	  */
	override def init(step: FormatStep, template: String, env: Map[String, Any]): Unit = {
		log.info("FormatPlugin's init is start.")
		this.inHdfsDir = this.getInHdfsDir(step.retDataFinger)
		log.debug("FormatPlugin's inHdfsDir is " + this.inHdfsDir)
		this.inFieldDelimiter = env.getOrElse("filedDelimiter", PluginEnv.plugFieldDelimiter).toString
		this.outCode = step.encode.toString
		this.outFieldDelimiter = PluginEnv.plugFrmtFieldDelimiter
		this.formatType = step.format.formType.toString.toLowerCase
		this.outDir  = this.getOutDir(step.retFormatFinger)
		this.outHdfsDir = this.getOutHdfsDir(step.retFormatFinger)
		this.format = step.format

		this.checkFileName = BaseUtil.getName(step.retFormatFinger, Some(BaseUtil.getTPathType(this.formatType)), None, Some(SuffixType.CHK))

		this.titleFileName = BaseUtil.getName(step.retFormatFinger, Some(BaseUtil.getTPathType(this.formatType)), None, Some(SuffixType.TIL))
		this.titleContext = env.getOrElse("titleContext", SqlUtil.projColumns(step.repArgs)).toString
		this.processCmd = BaseUtil.replace(template, this.assemble)
		log.debug("FormatPlugin cmd: {}", this.processCmd)
		log.info("FormatPlugin's init is End.")


	}

	/**
	  * 0、删除输出文件夹
	  * 1、执行封装命令
	  * 2、生成title文件
	  * 3、生成校验文件
	  * @return
	  */
	override def execute(): Int = {
		log.info("FormatPlugin's excute is start. -- cmd context is " + this.processCmd)
		var res:Int = 0
		val operCmd = OperCmd.apply()
		res = operCmd.runCmd(this.processCmd)
//		this.checkFileName = FileUtil.createCheckFile(this.outDir, Encode.GBK)
		// init 已经生成CHK file名
		OperFileUtil.getCheckFile(this.outDir, this.checkFileName, Some(PluginEnv.cacheFileSystem))
		this.formatType.toString.toUpperCase match {
//			case "TXT"		=> 	this.titleFileName = FileUtil.createTitleFile(this.outDir, this.titleContext)
			case "TXT"		=> 	OperFileUtil.writeFile(this.titleFileName, this.titleContext, Some(PluginEnv.cacheFileSystem))
			case "JSON"		=> 	log.info("Don't need to generate title file.")
			case "XML"		=>	log.info("Don't need to generate title file.")
			case _			=>	log.info("formatType is not matching.")
		}
		log.info("FormatPlugin's excute is end")
		setOut
		res
	}

	def getEncode(str: String): String = {
		val parts = str split "\\."
		var re:String = ""
		if (parts.length == 2) re = parts(1) else re = ""
		re
	}

	def assemble(): mutable.Map[String,String] = {
		println("assemble start...")
		val paraMap = new mutable.HashMap[String, String]()
		paraMap += ("JARPATH" -> PluginEnv.plugFrmtJarPath)
		paraMap += ("libJars" -> PluginEnv.plugFrmtLibJars)
		paraMap += ("inHdfsDir" -> this.inHdfsDir.trim)
		paraMap += ("inFieldDelimiter" -> this.inFieldDelimiter.trim)
		paraMap += ("outHdfsDir" -> this.outHdfsDir.trim)
		paraMap += ("outCode" -> this.outCode.trim)
		paraMap += ("outFieldDelimiter" -> this.outFieldDelimiter.trim)
		paraMap += ("format" -> Json.generate(this.format))
		paraMap += ("titleContext" -> this.titleContext.trim)

		println("assemble end...")

/*		for ((e1, e2) <- paraMap.toSet) {
			println("paraMap contains " + e1 + " : " + e2)
		}*/

		paraMap
	}

	def setOut = {
		this.out += ("outHdfsDir"	-> this.outHdfsDir)
		this.out += ("checkFile" 	-> this.checkFileName)
		this.out += ("titleFile"	-> this.titleFileName)
		this.out += ("filePackLoc"	-> this.outHdfsDir)
	}
}
/*
object testFormat extends App {
	val template = "hadoop jar ${JARPATH} ${inPath} ${inCode} ${inFieldDelimiter} ${outPath} ${outCode} ${outFieldDelimiter} ${formatType} ${titleContext}"
	println("the FormatPlugin is start")
	val fp = new FormatPlugin()
	println("the FormatPlugin is construct")
	val repArg1 = new RepArg("deal_date","deal_date","int","处理日期","pmid3","TB_MID_LOC_CMCC_USER_201504","test1")
	val repList = List(repArg1)
	val sh = mutable.HashMap(
			"filePath" -> "/var/opt/hdfs/format"
		,	"fileCode" -> "UTF-8"
		,	"titleContext" -> "deal_date,user_id"
		,	"filedDelimiter" -> "\u007c"
		,	"ftch_targetfilename" -> "outfile"
		,	"fingerPath"	-> "/user/opai/data"
	)




	val step = FormatStep(Format.XML, Encode.GBK, repList, "infile", "outfile")
	fp.init(step, template, sh.toMap)
	fp.execute()
}*/


