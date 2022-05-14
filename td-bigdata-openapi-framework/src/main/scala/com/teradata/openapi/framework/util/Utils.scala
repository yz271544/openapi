package com.teradata.openapi.framework.util

import java.net._
import java.util
import java.util.Collections

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{RepArg, ReqArg, ReqToFind}
import com.teradata.openapi.framework.exceptions.OpenApiException
import com.xiaoleilu.hutool.util.SecureUtil
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang3.SystemUtils

import scala.collection.JavaConversions._
import scala.collection.SortedMap
import scala.util.Try
import scala.util.control.ControlThrowable

/**
  * Created by Administrator on 2016/3/31.
  */
private[openapi] object Utils extends OpenApiLogging{

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
        throw new OpenApiException("无效的 master URL: " + openApiUrl)
      }
      (host,port)
    }catch {
      case e: java.net.URISyntaxException =>
        throw new OpenApiException("无效的 master URL: " + openApiUrl,e)
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

  def tryOrExit(block: => Unit) {
    try {
      block
    } catch {
      case e: ControlThrowable => throw e
      case t: Throwable => OpenApiUncaughtExceptionHandler.uncaughtException(t)
    }
  }

  private var customHostname: Option[String] = sys.env.get("OPEANAPI_LOCAL_HOSTNAME")

  /**
    * Get the local machine's hostname.
    */
  def localHostName(): String = {
    customHostname.getOrElse(localIpAddressHostname)
  }

  /**
    * Whether the underlying operating system is Windows.
    */
  val isWindows = SystemUtils.IS_OS_WINDOWS

  def getAddressHostName(address: String): String = {
    InetAddress.getByName(address).getHostName
  }


  lazy val localIpAddress:String = findLocalIpAddress()
  lazy val localIpAddressHostname: String = getAddressHostName(localIpAddress)


  private def findLocalIpAddress():String = {
    val defaultIpOverride = System.getenv("OPENAPI_LOCAL_IP")
    if(defaultIpOverride != null){
      defaultIpOverride
    }else{
      //windows下返回 "Evan-PC/192.168.142.1"
      //Linux下返回 "127.0.0.1"
      println("------1-----")

      logDebug("进入findLocalIpAddress.....")

      val address: InetAddress = InetAddress.getLocalHost //Returns the address of the local host

      if(address.isLoopbackAddress){ //
      val activeNetworkIFs = NetworkInterface.getNetworkInterfaces.toList
        val reOrderedNetworkIFs = if (isWindows) activeNetworkIFs else activeNetworkIFs.reverse
        for (ni <- reOrderedNetworkIFs) {
          for (addr <- ni.getInetAddresses if !addr.isLinkLocalAddress &&
            !addr.isLoopbackAddress && addr.isInstanceOf[Inet4Address]) {
            // We've found an address that looks reasonable!
            logWarning("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
              " a loopback address: " + address.getHostAddress + "; using " + addr.getHostAddress +
              " instead (on interface " + ni.getName + ")")
            logWarning("Set SPARK_LOCAL_IP if you need to bind to another address")
            return addr.getHostAddress
          }
        }
        logWarning("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
          " a loopback address: " + address.getHostAddress + ", but we couldn't find any" +
          " external IP address!")
        logWarning("Set SPARK_LOCAL_IP if you need to bind to another address")
      }
      logDebug("本地 address" + address.getHostAddress)
      address.getHostAddress//返回具体的IP地址
    }
  }

  def checkHost(host:String,message:String = ""){
    assert(host.indexOf(':') == -1,message)
  }

  def finger(toFind: ReqToFind, isFormat: Boolean):String = {
    var paramValues = SortedMap[String, Any]()
    paramValues += ("apiId" -> toFind.apiId)
    paramValues += ("apiVersion" -> toFind.api_version)
    if (toFind.isSyn equals Constants.REQ_TYPE_0) {
      if (toFind.pageNum != null && toFind.pageSize != null) {
        paramValues += (Constants.PAGE_NUM -> toFind.pageNum)
        paramValues += (Constants.PAGE_SIZE -> toFind.pageSize)
      }
    }
    val reqArgs: List[ReqArg] = toFind.reqArgs
    import scala.collection.JavaConversions._
    for (reqArg <- reqArgs) {
      val aa: String = reqArg.calcPrincId + Constants.JOIN_SIGN + StringUtils.join(reqArg.fieldValue, Constants.SPLIT_SIGN)
      paramValues += (reqArg.fieldName -> aa)
    }
    val repArgs: List[RepArg] = toFind.repArgs
    val repParam: util.List[String] = new util.ArrayList[String]
    import scala.collection.JavaConversions._
    for (repArg <- repArgs) {
      repParam.add(repArg.fieldName)
    }
    Collections.sort(repParam)
    paramValues += (SystemParameterNames.FIELDS -> StringUtils.join(repParam, Constants.SPLIT_SIGN))
    if (isFormat) {
      paramValues += (SystemParameterNames.FORMAT -> FastJSONUtil.serialize(toFind.format))
      paramValues += (SystemParameterNames.CODE_TYPE -> toFind.encode)
    }
    val sb: StringBuilder = new StringBuilder
    val paramNames: util.List[String] = new util.ArrayList[String](paramValues.size)
    paramNames.addAll(paramValues.keySet)
    Collections.sort(paramNames)
    import scala.collection.JavaConversions._
    for (paramName <- paramNames) {
      sb.append(paramName).append(paramValues.get(paramName))
    }
    SecureUtil.md5(sb.toString, Constants.UTF8)
  }
}


























