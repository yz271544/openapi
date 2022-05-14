package com.teradata.openapi.framework

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap

/**
  * 整个集群全局的配置信息
  * Created by Administrator on 2016/3/31.
  */
class OpenApiConf(loadDefaults:Boolean) extends Cloneable with OpenApiLogging{

  /** Create a OpenApiConf that loads defaults from system properties and the classpath*/
  def this() = this(true)

  private val settings = new HashMap[String,String]()

  if (loadDefaults) {
    // Load any openapi.* system properties
    for ((k, v) <- System.getProperties.asScala if k.startsWith("openapi.")) {
      settings(k) = v
    }
  }

  /** Set a configuration variable. */
  def set(key:String,value:String):OpenApiConf ={
    if (key == null) {
      throw new NullPointerException("null key")
    }
    if (value == null) {
      throw new NullPointerException("null value")
    }
    settings(key) = value
    this
  }

  /** Get a parameter; throws a NoSuchElementException if it's not set */
  def get(key: String): String = {
    settings.getOrElse(key, throw new NoSuchElementException(key))
  }

  /** Get a parameter, falling back to a default if not set */
  def get(key: String, defaultValue: String): String = {
    settings.getOrElse(key, defaultValue)
  }

  /** Get a parameter as an Option */
  def getOption(key: String): Option[String] = {
    settings.get(key)
  }

  /** Get all parameters as a list of pairs */
  def getAll: Array[(String, String)] = settings.clone().toArray

  /** Get a parameter as an integer, falling back to a default if not set */
  def getInt(key: String, defaultValue: Int): Int = {
    getOption(key).map(_.toInt).getOrElse(defaultValue)
  }

  /** Get a parameter as a long, falling back to a default if not set */
  def getLong(key: String, defaultValue: Long): Long = {
    getOption(key).map(_.toLong).getOrElse(defaultValue)
  }

  /** Get a parameter as a double, falling back to a default if not set */
  def getDouble(key: String, defaultValue: Double): Double = {
    getOption(key).map(_.toDouble).getOrElse(defaultValue)
  }

  /** Get a parameter as a boolean, falling back to a default if not set */
  def getBoolean(key: String, defaultValue: Boolean): Boolean = {
    getOption(key).map(_.toBoolean).getOrElse(defaultValue)
  }

  /** Get all akka conf variables set on this OpenApiConf */
  def getAkkaConf: Seq[(String, String)] = getAll.filter {case (k, v) => k.startsWith("akka.")}


  /**
    * Return a string listing all keys and values, one per line. This is useful to print the
    * configuration out for debugging.
    */
  def toDebugString: String = {
    settings.toArray.sorted.map{case (k, v) => k + "=" + v}.mkString("\n")
  }

}

object OpenApiConf extends OpenApiLogging{
  def main(args: Array[String]) {
     val config = new OpenApiConf()
     config.set("openapi.test","hello openapi")
     println(config.get("openapi.test"))
  }
}
