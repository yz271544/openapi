package com.teradata.openapi.framework.util.pluginUtil

import java.io.File

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Encode
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.TimeFuncChk.{getCurrentTimeMillis, timeMillsToTimeStamp}
import com.teradata.openapi.framework.util.UUIDUtils

import scala.collection.mutable
import scala.language.implicitConversions
import scala.languageFeature.implicitConversions
import scala.util.matching.Regex._

/**
  * Created by hdfs on 2016/5/19.
  */
private[openapi] object BaseUtil extends OpenApiLogging {
	/**
	  * Set implicit convert from String to File
	  **/
	implicit def String2File(value: String): File = new java.io.File( value )

	private final val suffixRegexString: String = "\\.[a-zA-Z]+$"

	/**
		* modify(delete) by qiwei 2018-01-02
	  * 建文件的绝对路径转换为对应Fuse绝对路径
	  * param path 需要绝对路径
	  * return Fuse的绝对路径 可以是文件夹或者文件
	  */
	/*def cvtFusepath(path: String): String = this.genDir( PluginEnv.plugSysFuseDir ) + path*/
	/**
		* modify(delete) by qiwei 2018-01-02
	  * 将文件的Fuse的相对路径转换为 hadoop fs -ls / 本地路径为根目录的路径
	  * param path
	  * return
	  */
	/*def cvtlocalPath(path: String): String = path.replace( PluginEnv.plugSysFuseDir, "" )*/


	/**
		* modify(add) by qiwei 2018-01-02
		* 建文件的绝对路径转换为对应Fuse绝对路径
		* @param path 需要绝对路径
		* @return Fuse的绝对路径 可以是文件夹或者文件
		*/
	def cvtFusepath(path: String): String = {
		(PluginEnv.plugHDFSUri.endsWith(PluginEnv.plugFileDelimiter), path.startsWith(PluginEnv.plugFileDelimiter)) match {
			case (true, true) => PluginEnv.plugHDFSUri + path.replaceAll("^"+PluginEnv.plugFileDelimiter, "")
			case (false, true) => PluginEnv.plugHDFSUri + path
			case (true, false) => PluginEnv.plugHDFSUri + path
			case (false, false) => PluginEnv.plugHDFSUri + PluginEnv.plugFileDelimiter + path
		}

	}
	/**
		* modify(add) by qiwei 2018-01-02
		* 将文件的Fuse的相对路径转换为 hadoop fs -ls / 本地路径为根目录的路径
		* @param path
		* @return
		*/
	def cvtlocalPath(path: String): String = {
		var temp:String = path.replace( PluginEnv.plugHDFSUri, "" )
		if(temp.startsWith(PluginEnv.plugFileDelimiter)) temp = PluginEnv.plugFileDelimiter + temp
		temp
	}


	// 路径补分割线
	private[this] def genDir(dirPath: String): String = if (dirPath.endsWith( PluginEnv.plugFileDelimiter )) dirPath else dirPath + PluginEnv.plugFileDelimiter
	// 路径补分割线
	private[this] def genDir(absPath: String, dirName: String): String = this.genDir( genDir( absPath ) + dirName )
	// 获取文件全路径名
	private[this] def genName(absPath: String, dirName: String): String = genDir( absPath ) + dirName
	// 文件加后缀
	def getAddSuffix(name: String, suffix : String): String = name + "." + suffix

	/**
	  * 给文件名后面增加后缀，只有文件名，没有路径信息
	  * @param name
	  * @param suffix
	  * @param randomFlag
	  * @return
	  */
	def getAddSuffix(name: String, suffix: SuffixType.Value, randomFlag: Boolean = false): String = {
		val fname = if (randomFlag.equals( true )) name + UUIDUtils.getRandomNum( 4 ) else name
		suffix match {
			case SuffixType.CHK => getAddSuffix( fname, SuffixType.CHK.toString )
			case SuffixType.TIL => getAddSuffix( fname, SuffixType.TIL.toString )
			case SuffixType.CMD => getAddSuffix( fname, SuffixType.CMD.toString )
		}
	}

	def getAddSuffix(name: String, suffix: Encode.Value): String = {
		suffix match {
			case Encode.GBK => getAddSuffix( name, Encode.GBK.toString )
			case Encode.UTF8 => getAddSuffix( name, Encode.UTF8.toString )
		}
	}

	def getCmdFileName(finger: String): String = {
		this.genName( PluginEnv.plugTmpCmdDir, this.getAddSuffix( finger, SuffixType.CMD ) )
	}

	def getTargetPath(tPathType: TPathType.Value): String = {
		val dic: String = tPathType match {
			case TPathType.TXT => TPathType.TXT.toString.toLowerCase
			case TPathType.JSON => TPathType.JSON.toString.toLowerCase
			case TPathType.XML => TPathType.XML.toString.toLowerCase
			case TPathType.XLS => TPathType.XLS.toString.toLowerCase
			case TPathType.NORMAL => TPathType.NORMAL.toString.toLowerCase
		}
		this.genDir( PluginEnv.hdfsWorkDir, dic ) // 返回文件目录的路径，非文件的绝对路径
	}

	def getTargetPath(tPathTypeValue: TPathType.Value, finger: String): String = {
		this.genDir( this.getTargetPath( tPathTypeValue ), finger )
	}

	// Format : /user/oapi/data/normal/$finger/$finger.GBK
	def getNormalPath(finger: String, encode:Encode.Value): String = {
		this.genName( this.genDir( PluginEnv.plugFtchTargetDir, finger ), this.getAddSuffix( finger, encode) )
	}


	// Format : /data/openapi/APP/
	def getLocalPath(finger: String, encode:Encode.Value): String = {
		this.genName( PluginEnv.plugTmpCachDir, this.getAddSuffix( finger, encode ) )
	}

	def getTableName(scheme:String, name:String, prefix:String=PluginEnv.plugTmpTableNamePrefix ): String = scheme + "." + prefix + name

	/**
	  *
	  * @param name			名称 			可能是路径名称也可能是参数名称
	  * @param targetDir		目标路径名		如果为None表示本地路径名，如果为非None表示HDFS对应路径(相对路径)
	  * @param encode			编码格式		数据文件导出编码。非数据文件则为None
	  * @param suffix			文件后缀		cmd为本地要执行命令， TIL为导出表头内容， CHK为校验文件
	  * @return				返回值为Unit
	  */
	def getName(name : String, targetDir : Option[TPathType.Value], encode : Option[Encode.Value], suffix: Option[SuffixType.Value] ) = {
		encode match {
			/**
			  * 编码不为空
			  * 为数据文件
			  */
			case Some(encodeName) => {
				suffix match {
					case Some(x) => throw new Error( "Two suffix conflict." )
					case None => {
						targetDir match {
							case Some( TPathType.NORMAL ) => this.getNormalPath( name, encodeName )
							case Some( x ) => throw new Error( "Data file is not store in !Normal dictionary." )
							case None 	=> this.getLocalPath(name, encodeName)
						}
					}
				}
			}

			/**
			  * 编码为空
			  * 	后缀为CMD 命令文件	路径为本地
			  * 	后缀为TIL title文件	路径为HDFS路径的TXT中
			  * 	后缀后CHK 校验文件	路径为HDFS路径
			  * 	后缀为空
			  * 	目标路径 非文件
			  */
			case None => {
				suffix match {
					case Some(SuffixType.CMD) => this.getCmdFileName(name)
					case Some(suf) => {
						targetDir match {
							case Some(tarName) => {
								this.genName(this.getTargetPath(tarName), this.getAddSuffix(name, suf, false))
							}
							case None => 	throw new Error( "CHK and TIL data is not in local Path." )
						}
					}
					case None => {
						targetDir match {
							case Some( x ) => this.getTargetPath( x, name )
							case None => {
								if (name.equals("")) {
									this.genDir(PluginEnv.hdfsWorkDir)
								}
								else {
									throw new Error( "Dic path is error" )
								}
							}
						}
					}
				}
			}
		}
	}



	/**
	  * Replace param from Map
	  * @param template
	  * @param mapPara
	  * @return
	  */
	def replace(template: String, mapPara: mutable.Map[String, String]): String = {
		println( "begin replace :" + mapPara.getClass )
		val paraPattern = "\\$\\{([^}]+)\\}".r
		println( mapPara )
		val mapper = (m: Match) => mapPara get (m group 1) map (quoteReplacement( _ ))
		paraPattern replaceSomeIn(template, mapper)
	}

	/**
	  * Replace param from Map
	  * @param template
	  * @param map
	  * @return
	  */
	def replace2(template: String, map: Map[String, Any]): String = {
		val mm: mutable.Map[String, String] = mutable.HashMap( )
		for ((k, v) <- map) {
			mm += (k -> v.toString)
		}
		replace( template, mm )
	}

	/**
	  * Get SourceInfoString Convert to SourceInfoRow Object
	  * 4, 04, 4,OpenAPI元数据库, 192.168.20.1, 5432, open_api, open_api, 123456, 0, 0,0,org.postgresql.Driver,true,true,true
	  * @param sourceInfo
	  * @return
	  */
	def getSourceInfo(sourceInfo: String): SourceInfoRow = {
		val rowArray = sourceInfo.split( ',' )
		new SourceInfoRow(
			rowArray( 0 ).toString.trim.toInt //source_id
			, rowArray( 1 ).toString.trim //source_type_code
			, rowArray( 2 ).toString.trim.toInt //drv_code
			, rowArray( 3 ).toString.trim //source_desc
			, rowArray( 4 ).toString.trim //ip_addr
			, rowArray( 5 ).toString.trim.toInt //port
			, rowArray( 6 ).toString.trim //deflt_schema
			, rowArray( 7 ).toString.trim //user_name
			, rowArray( 8 ).toString.trim //pwd
			, rowArray( 9 ).toString.trim.toInt //priority
			, rowArray( 10 ).toString.trim.toInt //sync_strategy_id
			, rowArray( 11 ).toString.trim.toInt //asyn_strategy_id
			, rowArray( 12 ).toString.trim //drv_name
			, rowArray( 13 ).toString.trim.toBoolean //sync_finder_flag
			, rowArray( 14 ).toString.trim.toBoolean //asyn_finder_flag
			, rowArray( 15 ).toString.trim.toBoolean //rss_finder_flag
		)
	}

	/**
	  * 替换命令中的目标路径参数，主要是为了处理未提供Fuse的系统,修改文件导出位置到临时表
	  * ，然后通过Put命令将本地文件导入HDFS中
	  * @param cmdContext
	  * @param targetDir
	  * @return
	  */
	def getCmdReTargetCmd(cmdContext: String, targetDir: String):String = {
		cmdContext.replace( """${targetPath}""", targetDir )
	}

	/**
	  * 通过输出文件类型获取枚举值类型
	  * @param fileFrmtType
	  */
	def getTPathType(fileFrmtType:String): TPathType.Value ={
		fileFrmtType.toUpperCase match {
			case "XML" => TPathType.XML
			case "XLS" => TPathType.XLS
			case "TXT" => TPathType.TXT
			case "JSON" => TPathType.JSON
		}
	}

	def splitDicAndPath(fullPath: String ): mutable.HashMap[String, String] = {
		val file = new File(fullPath)
		val reMap = new mutable.HashMap[String, String]()
		if(file.isDirectory) {
			reMap += ("dic" -> file.getPath)
			reMap += ("file" -> "")
		}
		else {
			reMap += ("dic" -> file.getParent)
			reMap += ("file" -> file.getName)
		}
		reMap
	}

	def rmSuffix(filePath: String): String = {
		filePath.replaceFirst(this.suffixRegexString, "")
	}

	def getDateString():String = {
		timeMillsToTimeStamp(getCurrentTimeMillis)
	}

}


/**
  * 目标路径枚举类型 齐伟  ADD 2017/5/18 18:23:10
  */
object TPathType extends Enumeration {
	type TPathType = Value
	val TXT, JSON, XML, XLS, NORMAL = Value
}

/*object Encode extends Enumeration {
	type Encode = Value
	val GBK, UTF8 = Value
}*/

/**
  * 文件后缀枚举类型 齐伟 ADD 2017/5/18 18:23:10
  */
object SuffixType extends Enumeration {
	type SuffixType = Value
	val CHK, TIL, CMD = Value
}


