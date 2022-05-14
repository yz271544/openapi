package com.teradata.openapi.framework.util.pluginUtil

import java.io._
import java.util.Properties

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.util.TimeFunc
import org.apache.hadoop.fs.{FileSystem, _}

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by sagmain on 2017/5/9.
  */
object HDFSPathHelper extends OpenApiLogging with TimeFunc{
	private final val SUCCESS = "_SUCCESS"

	/**
	  * uri 参数 需要用80020端口，如果使用9000端口会报错
	  */

	def isDir(name : String, hdfs : FileSystem ) : Boolean = {
		hdfs.isDirectory(new Path(name))
	}
	def isDir(name : Path, hdfs : FileSystem ) : Boolean = {
		hdfs.isDirectory(name)
	}
	def isFile(name : String, hdfs : FileSystem ) : Boolean = {
		hdfs.isFile(new Path(name))
	}
	def isFile(name : Path, hdfs : FileSystem ) : Boolean = {
		hdfs.isFile(name)
	}
	def createFile(name : String, hdfs : FileSystem ) : Boolean = {
		hdfs.createNewFile(new Path(name))
	}
	def createFile(name : Path, hdfs : FileSystem ) : Boolean = {
		hdfs.createNewFile(name)
	}
	def createFolder(name : String, hdfs : FileSystem ) : Boolean = {
		hdfs.mkdirs(new Path(name))
	}
	def createFolder(name : Path, hdfs : FileSystem ) : Boolean = {
		hdfs.mkdirs(name)
	}
	def exists(name : String, hdfs : FileSystem ) : Boolean = {
		hdfs.exists(new Path(name))
	}
	def exists(name : Path, hdfs : FileSystem ) : Boolean = {
		hdfs.exists(name)
	}

	def deleteFolder(name: String, hdfs : FileSystem): Boolean = {
		hdfs.delete(new Path(name), true)
	}
	def deleteFolder(path: Path , hdfs : FileSystem): Boolean = {
		hdfs.delete(path, true)
	}
	def transport(inputStream : InputStream, outputStream : OutputStream): Unit ={
		val buffer = new Array[Byte](64 * 1000)
		var len = inputStream.read(buffer)
		while (len != -1) {
			outputStream.write(buffer, 0, len - 1)
			len = inputStream.read(buffer)
		}
		outputStream.flush()
		inputStream.close()
		outputStream.close()
	}
	class MyPathFilter extends PathFilter {
		override def accept(path: Path): Boolean = true
	}

	/**
	  * create a target file and provide parent folder if necessary
	  */
	def createLocalFile(fullName : String) : File = {
		val target : File = new File(fullName)
		if(!target.exists){
			val index = fullName.lastIndexOf(File.separator)
			val parentFullName = fullName.substring(0, index)
			val parent : File = new File(parentFullName)

			if(!parent.exists)
				parent.mkdirs
			else if(!parent.isDirectory)
				parent.mkdir

			target.createNewFile
		}
		target
	}


	/**
	  * delete file in hdfs
	  * @return true: success, false: failed
	  */
	def deleteFile(path: String, hdfs : FileSystem )  : Boolean = {
		if (isDir(path,hdfs))
			hdfs.delete(new Path(path), true)//true: delete files recursively
		else
			hdfs.delete(new Path(path), false)
	}

	/**
	  * get all file children's full name of a hdfs dir, not include dir children
	  * @param fullName the hdfs dir's full name
	  */
	def listChildren(fullName : String, holder : ListBuffer[String], hdfs : FileSystem ) : ListBuffer[String] = {
		val filesStatus = hdfs.listStatus(new Path(fullName), new MyPathFilter)
		for(status <- filesStatus){
			val filePath : Path = status.getPath
			if(isFile(filePath, hdfs))
				holder += filePath.toString
			else
				listChildren(filePath.toString, holder, hdfs)
		}
		holder
	}


	def copyFile(source: String, target: String, hdfs : FileSystem ): Unit = {

		val sourcePath = new Path(source)
		val targetPath = new Path(target)

		if(!exists(targetPath, hdfs))
			createFile(targetPath, hdfs)

		val inputStream : FSDataInputStream = hdfs.open(sourcePath)
		val outputStream : FSDataOutputStream = hdfs.create(targetPath)
		transport(inputStream, outputStream)
	}

	def copyFolder(sourceFolder: String, targetFolder: String, hdfs : FileSystem ): Unit = {
		val holder : ListBuffer[String] = new ListBuffer[String]
		val children : List[String] = listChildren( sourceFolder, holder, hdfs).toList
		for(child <- children)
			copyFile(child, child.replaceFirst(sourceFolder, targetFolder), hdfs)
	}

	def copyFileFromLocal(localSource: String, hdfsTarget: String, hdfs : FileSystem ): Unit = {
		val targetPath = new Path(hdfsTarget)
		if(!exists(targetPath, hdfs))
			createFile(targetPath, hdfs)

		val inputStream : FileInputStream = new FileInputStream(localSource)
		val outputStream : FSDataOutputStream = hdfs.create(targetPath)
		transport(inputStream, outputStream)
	}

	def loadInputStreamFromHDFS(localSource: String, hdfs : FileSystem ): FSDataInputStream = {
		val inputStream : FSDataInputStream = hdfs.open(new Path(localSource))
		inputStream
	}

	def copyFileToLocal(hdfsSource: String, localTarget: String, hdfs : FileSystem ): Unit = {
		val localFile : File = createLocalFile(localTarget)

		val inputStream : FSDataInputStream = hdfs.open(new Path(hdfsSource))
		val outputStream : FileOutputStream = new FileOutputStream(localFile)
		transport(inputStream, outputStream)
	}

	def copyFolderFromLocal(localSource: String, hdfsTarget: String, hdfs : FileSystem ): Unit = {
		val localFolder : File = new File(localSource)
		val allChildren : Array[File] = localFolder.listFiles
		for(child <- allChildren){
			val fullName = child.getAbsolutePath
			val nameExcludeSource : String = fullName.substring(localSource.length)
			val targetFileFullName : String = hdfsTarget + Path.SEPARATOR + nameExcludeSource
			if(child.isFile)
				copyFileFromLocal(fullName, targetFileFullName, hdfs)
			else
				copyFolderFromLocal(fullName, targetFileFullName, hdfs)
		}
	}

	def copyFolderToLocal(hdfsSource: String, localTarget: String, hdfs : FileSystem ): Unit = {
		val holder : ListBuffer[String] = new ListBuffer[String]
		val children : List[String] = listChildren(hdfsSource, holder, hdfs).toList
		val hdfsSourceFullName = hdfs.getFileStatus(new Path(hdfsSource)).getPath.toString
		val index = hdfsSourceFullName.length
		for(child <- children){
			val nameExcludeSource : String = child.substring(index + 1)
			val targetFileFullName : String = localTarget + File.separator + nameExcludeSource
			copyFileToLocal(child, targetFileFullName, hdfs)
		}
	}


	/**
	  *	get 		hdfs file's lines
	  *	@param 		hdfs FileSystem
	  *	@param 		name String
	  *	@return 	String
	  */
	def getHDFSFileLines(name : Path, hdfs : FileSystem ) : String = {
		Source.fromInputStream(hdfs.open(name)).getLines().length.toString
	}

	def getHDFSFileContextLines(name : Path, hdfs : FileSystem ): Iterator[String] = {
		Source.fromInputStream(hdfs.open(name)).getLines()
	}

	/**
		* get		hdfs file's size b
		* @param name  Path
		* @param hdfs  FileSystem
		* @return String
		*/
	def getHDFSFileSize(name : Path, hdfs : FileSystem ) : String = {
		hdfs.getContentSummary(name).getLength().toString
	}

	def getHDFSFileCreateTime(name:Path, hdfs:FileSystem) : String = {
		timeMillsToTimeStamp(hdfs.getFileStatus(name).getModificationTime)
	}

	/**
	  * get file check context
	  * @param 		name		Path
	  * @param 		hdfs		FileSystem
	  * @return		String		check context
	  */
	def getHDFSFileInfo(name : Path, hdfs : FileSystem ) : String = {
		val a = List(name.toString, getHDFSFileSize(name, hdfs), getHDFSFileLines(name, hdfs), getHDFSFileCreateTime(name, hdfs))
//		log.debug("helper : " + a.mkString("\t"))
		a.mkString("\t")
	}


	def getCheckContent(name : String, hdfs : FileSystem) : String = {
		val properties = new Properties(System.getProperties)
		var re = ""
		HDFSPathHelper.isDir(name, hdfs) match {
			case true		=>	{
				log.debug("--Hdfs check content	")
				for (elem <- HDFSPathHelper.listChildren(name, new ListBuffer[String], hdfs) if !(elem.toUpperCase.endsWith(SUCCESS))) {
					re += HDFSPathHelper.getHDFSFileInfo(new Path(elem.toString), hdfs) + properties.getProperty("line.separator")
				}
			}
			case false 	=> 	{
				log.debug( "++file system check content")
				re += HDFSPathHelper.getHDFSFileInfo(new Path(name), hdfs)
			}
		}
		re
	}
}
