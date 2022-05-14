package com.teradata.openapi.framework.util

import akka.actor.{ExtendedActorSystem, ActorSystem}
import com.teradata.openapi.framework.{OpenApiConf, OpenApiLogging}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * Created by Evan on 2016/3/31.
  */
object AkkaUtils extends OpenApiLogging{

  def createActorSystem(name:String,host:String,port:Int,conf:OpenApiConf):(ActorSystem,Int) = {

    val logAkkaConfig = if (conf.getBoolean("openapi.akka.logAkkaConfig", false)) "on" else "off"

    val akkaConf = ConfigFactory.parseMap(conf.getAkkaConf.toMap.asJava)
         .withFallback(ConfigFactory.parseString(
         s"""
         |akka.daemonic = on
         |akka.loggers = [""akka.event.slf4j.Slf4jLogger""]
         |akka.stdout-loglevel = "ERROR"
         |akka.jvm-exit-on-fatal-error = off
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.transport-class = "akka.remote.transport.netty.NettyTransport"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = $port
         |akka.remote.netty.tcp.tcp-nodelay = on
         |akka.log-config-on-start = $logAkkaConfig
         """.stripMargin))
    val actorSystem = ActorSystem(name,akkaConf)
    val provider = actorSystem.asInstanceOf[ExtendedActorSystem].provider
    val boundPort = provider.getDefaultAddress.port.get
    (actorSystem, boundPort)
  }

  def askTimeout(conf:OpenApiConf):FiniteDuration ={
    Duration.create(conf.getLong("openapi.akka.lookupTimeout", 30), "seconds")
  }

  def protocol(actorSystem: ActorSystem):String = {
    val akkaConf = actorSystem.settings.config
    val sslProp = "akka.remote.netty.tcp.enable-ssl"
    protocol(akkaConf.hasPath(sslProp) && akkaConf.getBoolean(sslProp))
  }

  def protocol(ssl: Boolean = false): String = {
    if (ssl) {
      "akka.ssl.tcp"
    } else {
      "akka.tcp"
    }
  }

  def address(
     protocol: String,
     systemName: String,
     host: String,
     port: Any,
     actorName: String): String = {
    s"$protocol://$systemName@$host:$port/user/$actorName"
  }

}




























