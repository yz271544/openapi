package com.teradata.openapi.master.finder.tools

import java.io.{File, FileInputStream, InputStream}
import java.util.Properties

import bsh.commands.dir
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.util.Utils

/**
  * Created by John on 2016/7/20.
  */
class ToolApiLoader(classLoader: ClassLoader) extends ClassLoader(classLoader) with OpenApiLogging {

  val defaultToolApisProps = "application.properties"

  def ini = {
    try {
      //文件要放到resource文件夹下
      //			val path = Thread.currentThread().getContextClassLoader.getResource(defaultPluginProps).getPath
      //			val b =  this.getClass.getClassLoader.getResource()
      val path = Utils.getOpenApiClassLoader.getResource(defaultToolApisProps).getPath
      properties.load(new FileInputStream(path))
    } catch {
      case e: Exception => log.debug(e.getMessage)
    }
  }

  val properties = new Properties()
  this.ini


  val toolApisDir: String = properties.getProperty("toolapi.dir")


  @throws[ClassNotFoundException]
  override def findClass(className: String): Class[_] = {
    var clazz: Class[_] = findLoadedClass(className)
    if (clazz != null) return clazz
    clazz = loadToolApiClass(className)
    if (clazz == null) {
      clazz = this.getParent.loadClass(className)
    }
    clazz
  }

  private def getBinResource(s: String, dir: String = ""): Array[Byte] = {
    val fileName = dir + (if (s.startsWith("/")) s else "/" + s)
    val is = if (dir == "") new FileInputStream(dir + fileName) else classOf[ToolApiLoader].getResourceAsStream(fileName)
    if (is == null) {
      return null
    }
    Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
  }

  private def getClassResource(className: String): Array[Byte] = {
    val fileName = if (className.startsWith("/")) className else "/" + className
    val is: InputStream = classOf[ToolApiLoader].getResourceAsStream(fileName)
    if (is == null) {
      return null
    }
    Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
  }


  @throws[ClassNotFoundException]
  private def loadToolApiClass(className: String): Class[_] = {
    log.debug("className:" + className)
    val className0 = className.replace('.', '/') + ".class"
    log.debug("className0:" + className0)
    var fileBytes: Array[Byte] = getClassResource(className0)
    if (fileBytes == null) {
      fileBytes = getBinResource(className0,toolApisDir)
    }
    if (fileBytes == null) {
      throw new ClassNotFoundException(className0)
    }
    toolApiLoad(fileBytes, className)
  }


  def toolApiLoad(fileBytes: Array[Byte], className0: String): Class[_] = {
    this.defineClass(className0, fileBytes, 0, fileBytes.length)
  }
}


object ToolApiLoader {
  def apply(classLoader: ClassLoader): ToolApiLoader = new ToolApiLoader(classLoader)

  def apply(): ToolApiLoader = new ToolApiLoader(getClass.getClassLoader)

}

/*object ToolApiLoaderTest {
  def main(args: Array[String]) {

  }
}*/
