package com.teradata.openapi.util

import java.io.{File, FileInputStream}
import java.util.Properties
/**
  * Created by John on 2016/11/24.
  */
object HttpClientEnv {
  val defaultPluginProps ="httpclient.properties"
  def ini() = {
    try {
      val path = Utils.home + "/conf/" + defaultPluginProps
      //val path = Thread.currentThread().getContextClassLoader.getResource(defaultPluginProps).getPath
      properties.load(new FileInputStream(path))
    } catch {
      case e:Exception =>
        println("Can't init the Props from " + defaultPluginProps)
        throw e
    }
  }
  val properties = new Properties()
  this.ini()

  private val url = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
  private val path=
    try {
      new File(java.net.URLDecoder.decode(url,"utf8"))
    } catch {
      case e:Exception =>  new File(url)
    }

  val(libPath, mainPath)=
    if(path.isFile) {
      (path.getParent + File.separator, path.getParentFile.getParent + File.separator)
    }
    else {
      (path.getAbsolutePath + File.separator, path.getParent + File.separator)
    }


  val sourcePath  = properties.getProperty("exception.sourcePath")
}
