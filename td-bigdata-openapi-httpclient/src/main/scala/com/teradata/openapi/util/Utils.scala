package com.teradata.openapi.util

import java.io.File
import java.net._

import scala.util.Try

/**
  * Created by Administrator on 2016/3/31.
  */
object Utils {

  /**
    * Get the ClassLoader which loaded OpenApi
    *
    * @return
    */
  def getOpenApiClassLoader = getClass.getClassLoader

  /**
    * 获得当前线程级别的Context ClassLoader，如果不存在则载入Openapi 的全局ClassLoader
    *
    * @return
    */
  def getContextOrOpenApiClassLoader = Option(Thread.currentThread().getContextClassLoader).getOrElse(getOpenApiClassLoader)

  /**
    * 检查class是否在当前线程中
    *
    * @param clazz
    * @return
    */
  def classIsLoadable(clazz:String):Boolean ={
    Try{
      Class.forName(clazz,false,getContextOrOpenApiClassLoader)
    }.isSuccess
  }

  def classForName(className:String) = Class.forName(className,true,getContextOrOpenApiClassLoader)


  /**
    * 从openapiUrl中提取host和port，并以键值型返回
    *
    * @param openApiUrl
    * @return
    */
  def extractHostPortFromOpenApiUrl(openApiUrl:String):(String,Int)={
    try{
      val uri =  new java.net.URI(openApiUrl)
      val host = uri.getHost
      val port = uri.getPort
      if(uri.getScheme != "openapi" ||
        host == null ||
        port < 0 ||
        (uri.getPath != null && !uri.getPath.isEmpty) ||
        uri.getFragment != null ||
        uri.getQuery != null ||
        uri.getUserInfo != null){
        throw new RuntimeException("无效的 master URL: " + openApiUrl)
      }
      (host,port)
    }catch {
      case e: java.net.URISyntaxException =>
        throw new RuntimeException("无效的 master URL: " + openApiUrl,e)
    }
  }

  def inShutdown(): Boolean = {
    try {
      val hook = new Thread {
        override def run() {}
      }
      Runtime.getRuntime.addShutdownHook(hook)
      Runtime.getRuntime.removeShutdownHook(hook)
    } catch {
      case ise: IllegalStateException => return true
    }
    false
  }
  private var customHostname: Option[String] = sys.env.get("OPEANAPI_LOCAL_HOSTNAME")

  /**
    * Whether the underlying operating system is Windows.
    */

  def getAddressHostName(address: String): String = {
    InetAddress.getByName(address).getHostName
  }

  def checkHost(host:String,message:String = ""){
    assert(host.indexOf(':') == -1,message)
  }

  def home: String = {

    val url = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val path =
      try {
        new File(java.net.URLDecoder.decode(url,"utf8"))
      } catch {
        case e:Exception =>  new File(url)
      }

    if(path.isFile) {
      path.getParentFile.getPath
    }
    else {
      path.getPath
    }
  }
}


























