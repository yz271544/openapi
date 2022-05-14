package com.teradata.openapi.framework.util.pluginUtil

import java.text.SimpleDateFormat
import java.util.Date

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Encode
import com.teradata.openapi.framework.util.UUIDUtils

import scala.sys.process._

/**
  * Created by hdfs on 2017/5/14.
  */
private[openapi] class OperCmd extends OpenApiLogging {

	/**
	   .sh file input in VarFileExport
	  param targetDir
		param cmdContext
		return Int
		注释 by qiwei 2018-01-02
	def runCmdFile(targetDir: String, cmdContext: String, program: String = "td2hdfs"): Int = {
		var re: Int = 0
		log.debug( "cmdFullPath is " + targetDir + "----" + cmdContext )
		try {
			OperFileUtil.writeFile( targetDir, cmdContext, None )
			re = program match {
				case "td2hdfs" => "VarFileExport" #< new java.io.File( s"${targetDir}" ) !
			}
		}
		catch {
			case ex: Exception => println( "The VarFileExport's program is error." )
		}
		finally {
			OperFileUtil.rmdir( targetDir, None )
		}
		re
	}*/

	/**
		*	modify 2018-01-02 by Qiwei
		*	增加执行shell文件的命令，且参数program表示执行文件的内容，默认为shell，可以选择VarFileExport
		* @param targetDir
		* @param cmdContext
		* @param program
		* @return
		*/
	def runCmdFile(targetDir: String, cmdContext: String, program: String = "shell"): Int = {
		var re: Int = 0
		log.debug( "cmdFullPath is " + targetDir + "   ----   " + cmdContext  + BaseUtil.getDateString() )
		try {
			OperFileUtil.writeFile( targetDir, cmdContext, None )

			val result: String = program match {
				case "VarFileExport" => "VarFileExport" #< new java.io.File( s"${targetDir}" ) !!
				case "shell" => "sh" #< new java.io.File(s"${targetDir}") !!
			}
			log.info("The shell is finished....." + BaseUtil.getDateString())
			log.debug(result)
		}
		catch {
			case ex: Exception => println(s"The $program program is error.")
		}
		finally {
//			OperFileUtil.rmdir( targetDir, None )
/*			OperFileUtil.copyFile(targetDir, targetDir + ".bak", None)
			log.debug("------ copy time" + BaseUtil.getDateString())*/
			OperFileUtil.rmdir( targetDir, None )
			log.info("Delete cmd file" + targetDir.toString + "____" + BaseUtil.getDateString())
		}
		re
	}

	/**
	  * from local fileSystem to HDFS
	  * @param localPath
	  * @param hdfsPath
	  */
	def runHDFSPut(localPath: String, hdfsPath: String): Int = {
		try {
			HDFSPathHelper.copyFileFromLocal( localPath, hdfsPath, PluginEnv.cacheFileSystem )
		} catch {
			case e: Error => e.printStackTrace( )
		}
		0
	}

	/**
	  * running linux Cmd
	  * @param processCmd
	  * @return
	  */
	def runCmd(processCmd: String): Int = {
		var re: Int = 0
		try {
			//			log.debug("The FormatPlugin -> the excute -> the run -> context " + processCmd)
			val pb = Process( processCmd )
			log.debug( "Utils -> run -> Process :" + pb.toString )
			val b = pb !! ProcessLogger( out => {
				log.debug( out )
			} )
			log.info( "the run return staring = " + b )
			re
		}
		catch {
			case e: Exception => {
				re = 1
				log.debug( e.printStackTrace( ).toString )
				throw e
			}
		}
	}

	/**
	  * 运行外部命令，将结果导出到文件中
	  * @param processCmd
	  * @param outFileName
	  * @return
	  */
	def runCmd(processCmd: String, outFileName: String): Int = {
		var re: Int = 0
		try {
			re = s"${processCmd}" #> new java.io.File( s"${outFileName}" ) !
		}
		catch {
			case e: Exception => re = 1; log.debug( e.getMessage )
		}
		log.debug( "The run is end." + re )
		re
	}

	/**
	  * 判断是否使用fuse
	  * 使用fuse：
	  * 1、将命令内容中的目标文件替换
	  * 2、执行导数命令
	  * 3、数据直接进入HDFS对应的目录中
	  * 不是用fuse：
	  * 1、生成临时文件全路径
	  * 2、建命令中的目标文件替换为临时文件路径
	  * 3、执行导数命令
	  * 4、将本地文件系统文件导入到HDFS文件系统中
	  * 5、清理临时文件
	  * @param hdfsFilePath		目标路径
	  * @param tmpFilePath		文件名
	  * @param cmdContext		命令行内容
	  * @return				标识， 0 正常 1 错误
	  */
	def ftchT2Hdfs(hdfsFilePath: String, tmpFilePath: String, cmdContext: String, encode: Encode.Value=Encode.GBK) = {
		PluginEnv.plugUseFuseFlag match {
			case true => {
				val cmdString: String = BaseUtil.getCmdReTargetCmd( cmdContext, hdfsFilePath )
				this.runCmdFile( hdfsFilePath, cmdString )
			}
			case false => {
				val cmdString: String = BaseUtil.getCmdReTargetCmd( cmdContext, hdfsFilePath )
				this.runCmdFile( hdfsFilePath, cmdString )
				this.runHDFSPut( tmpFilePath, hdfsFilePath )
				OperFileUtil.rmdir( tmpFilePath, None )
			}
		}
		1
	}

	def ftchA2Hdfs(hdfsFilePath: String, cmdContext: String):Int = {
		/**
		  * Aster 数据库导出数据并加载进HDFS不管是否使用Fuse都使用临时文件中转
		  */
		PluginEnv.plugUseFuseFlag match {
			case true => {
				this.runCmd( cmdContext, hdfsFilePath)
			}
			case false => {
				val tmpFilePath = BaseUtil.getName(
						BaseUtil.splitDicAndPath( hdfsFilePath ).getOrElse("file", UUIDUtils.generateString(30)),
						targetDir = None,
						encode = Some( Encode.UTF8 ),
						suffix = None
				)
				log.debug("The temp file path is " + tmpFilePath)
				this.runCmd( cmdContext, tmpFilePath )
				log.debug("export data to tempFile")
				this.runHDFSPut( tmpFilePath, hdfsFilePath )
				OperFileUtil.rmdir( tmpFilePath, None )
			}
		}
		0
	}

}

private[openapi] object OperCmd {
	def apply(): OperCmd = new OperCmd()
}
