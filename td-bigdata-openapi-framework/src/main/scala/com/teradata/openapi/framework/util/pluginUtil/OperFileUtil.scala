package com.teradata.openapi.framework.util.pluginUtil

import java.io.{File, FileOutputStream, FileWriter, PrintWriter}
import java.util.Properties

import com.teradata.openapi.framework.OpenApiLogging
import org.apache.hadoop.fs.{FileSystem, Path}


/**
  * Created by hdfs on 2017/5/16.
  */
private [openapi] object OperFileUtil extends OpenApiLogging{

	implicit def string2HPath(value: String) = new Path(value)
	implicit def string2Path(value: String) = java.nio.file.Paths.get(value)
	implicit def string2File(value: String) = new File(value)

	def writeFile(fileName :String , fileContent: String, fileSystem: Option[FileSystem]): Boolean = {
		fileSystem match {
			case Some(hdfs) =>
				try {
					val titleFile = hdfs.create( string2HPath( fileName ) )
					log.debug("OperFileUtil -> getTitleFile -> titleFile is : " + fileName)
					titleFile.writeBytes(fileContent)
					titleFile.close()
				} catch {
					case e: Error => e.printStackTrace(); return false
				}
			case None	=>
				{
					val os = new FileOutputStream(fileName)
					val pw = new PrintWriter(os)
					pw.write(fileContent.replaceAll("[\\r]",""))
					pw.close()
					os.close()

				}
		}
		true
	}

	def deleteFile(fileName: String, fileSystem: Option[FileSystem]):Boolean = {
		fileSystem match {
			case Some(hdfs) => try {
				hdfs.deleteOnExit( fileName )
			} catch {
				case e: Error => e.printStackTrace(); false
			}
			case None =>  {
				PathHelper.RichPath( fileName ).delete( )
				true
			}
		}
	}

	def mkdir(path: String, fileSystem: Option[FileSystem]):Boolean = {
		fileSystem match {
			case Some(hdfs) => HDFSPathHelper.createFolder(path, hdfs)
			case None => 		{
				PathHelper.RichPath( path ).mkdirs
				true
			}
		}
	}

	def rmdir(path: String, fileSystem: Option[FileSystem]):Boolean = {
		fileSystem match {
			case Some(hdfs) => HDFSPathHelper.deleteFolder(path, hdfs)
			case None =>	{
				PathHelper.RichPath( path ).delete()
				true
			}
		}
	}

	/**
	  * 生成校验文件内容
	  * @param path				目录路径
	  * @param checkFileName		校验文件名
	  * @param fileSystem			文件系统(主要是调用不同的生成写文件函数)
	  * @return
	  */
	def getCheckFile(path: String, checkFileName:String , fileSystem: Option[FileSystem]=None): Boolean = {
		log.info("start create check file....")
		val properties = new Properties(System.getProperties)
		try {
			fileSystem match {
				case Some(hdfs) => {
					log.debug("Hdfs file check...")
					val checkContent = HDFSPathHelper.getCheckContent( path, hdfs ).toString
					log.debug("OperFileUtil -> getCheckFile -> checkContent is : " + checkContent)
					val checkfile = hdfs.create( string2HPath( checkFileName ) )
					log.debug("OperFileUtil -> getCheckFile -> checkfile is : " + checkfile)
					checkfile.writeBytes(checkContent)
					checkfile.close()
				}
				case None =>	this.writeFile(
					checkFileName,
					PathHelper.getCheckContent(string2Path(path)).mkString(properties.getProperty("line.separator")),
					None
				)
			}
		} catch {
			case e: Error => e.printStackTrace()
		}
		true
	}

	def getCheckFile(path: String, fileSystem: Option[FileSystem]): Boolean = {
		val checkFileName = BaseUtil.getAddSuffix(path, SuffixType.CHK)
		this.getCheckFile(path, checkFileName, fileSystem)
	}

	def copyFile(sourcePath:String, targetPath:String, fileSystem:Option[FileSystem]) : Boolean = {
		try {
			fileSystem match {
				case Some( hdfs ) => {
					hdfs.copyFromLocalFile( true, sourcePath, targetPath )
				}
				case None => PathHelper.RichPath( sourcePath ).copyTo( targetPath )
			}
		} catch {
			case e: Error => e.printStackTrace()
		}
		true
	}


}


