package com.teradata.openapi.master.controller

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.model.CountStrategyProperty
import it.sauronsoftware.cron4j.SchedulingPattern

import scala.collection.mutable

/**
  * Created by lzf on 2016/4/13.
  */
trait Strategy {

  def satisfied: Boolean

  def enable(enable: Boolean)

  def enter(task: Task): Strategy

  def distribute(): mutable.Seq[Task]

  def clean(task: Task): Strategy

  def loadProperty(pro:String): Unit

}

class CountStrategy extends Strategy {

  private val queue = mutable.TreeSet[Task]()
  @volatile
  private[controller] var enable = true
  private val process = mutable.Set[Task]()
  @volatile
  private var properties:Seq[CountStrategyProperty] = _

  override def satisfied: Boolean = {

    val maxCount = properties.find(pro=> {
      try{
        val sp = new SchedulingPattern(pro.moment)
        sp.`match`(System.currentTimeMillis())
      }catch{
        case e:Exception => false
      }

    }).getOrElse(CountStrategyProperty("",0)).maxCount

    process.size <= maxCount
  }

  def enter(task: Task): Strategy ={
    queue += task
    this
  }

  def distribute(): mutable.Seq[Task]= {

    val tasks = mutable.ArrayBuffer[Task]()
    val it = queue.iterator
    while(it.hasNext && this.satisfied) {
      val current = it.next()
      tasks += current
      process += current
      queue.remove(current)
    }
    tasks
  }

  def clean(task: Task): Strategy = {
    process.remove(task)
    this
  }

  override def enable(enable: Boolean): Unit = {
    this.enable = enable
  }

  override def loadProperty(pro: String): Unit = {
    try{
      properties = Json.parse[List[CountStrategyProperty]](pro)
    }catch {
      case e:Exception => e.printStackTrace()
    }
  }
}

