package openapi

import java.net.InetAddress
import java.util.concurrent.{Executors, ThreadPoolExecutor, ThreadFactory}

import com.google.common.util.concurrent.ThreadFactoryBuilder

/**
  * Created by Administrator on 2016/3/14.
  */
object Utils {

  private[openapi] val daemonThreadFactory: ThreadFactory =
    new ThreadFactoryBuilder().setDaemon(true).build()

  def newDaemonFixedThreadPool(nThreads:Int): ThreadPoolExecutor =
    Executors.newFixedThreadPool(nThreads, daemonThreadFactory).asInstanceOf[ThreadPoolExecutor]

  private var customHostname: Option[String] = None
  def localHostName(): String = {
    customHostname.getOrElse(InetAddress.getLocalHost.getHostName)
  }

}
