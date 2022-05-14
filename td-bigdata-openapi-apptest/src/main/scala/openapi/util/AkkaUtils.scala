package openapi.util

import akka.actor.{ExtendedActorSystem, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
  * Created by Administrator on 2016/3/15.
  * 使用Akka的工具类
  * 针对于远程调用，创建ActorSystem
  */
private[openapi] object AkkaUtils {

  def createActorSystem(name:String,host:String,port:Int):(ActorSystem,Int) = {
    val akkaThreads = System.getProperty("openapi.akka.threads","4").toInt
    val akkaBatchSize = System.getProperty("openapi.akka.batchSize", "15").toInt
    val akkaTimeout = System.getProperty("openapi.akka.timeout", "60").toInt
    val akkaFrameSize = System.getProperty("openapi.akka.frameSize", "10").toInt
    val lifecycleEvents = System.getProperty("openapi.akka.logLifecycleEvents", "false").toBoolean
    val akkaConf = ConfigFactory.parseString("""
      akka.daemonic = on
      akka.event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
      akka.stdout-loglevel = "ERROR"
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"
      akka.remote.transport = "akka.remote.netty.NettyRemoteTransport"
      akka.remote.netty.hostname = "%s"
      akka.remote.netty.port = %d
      akka.remote.netty.connection-timeout = %ds
      akka.remote.netty.message-frame-size = %d MiB
      akka.remote.netty.execution-pool-size = %d
      akka.actor.default-dispatcher.throughput = %d
      akka.remote.log-remote-lifecycle-events = %s
      """.format(host, port, akkaTimeout, akkaFrameSize, akkaThreads, akkaBatchSize,
      if (lifecycleEvents) "on" else "off"))
    val actorSystem = ActorSystem(name, akkaConf, getClass.getClassLoader)
    val provider = actorSystem.asInstanceOf[ExtendedActorSystem].provider
    val boundPort = provider.getDefaultAddress.port.get
    (actorSystem, boundPort)
  }

}






















