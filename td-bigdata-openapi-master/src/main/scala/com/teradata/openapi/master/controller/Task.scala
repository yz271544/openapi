package com.teradata.openapi.master.controller

import com.teradata.openapi.master.resolver._
import com.teradata.openapi.framework.step.Step

import scala.collection.mutable

/**
  * Created by lzf on 2016/4/11.
  */
class Task(val id: Int, val step: Step, var priority: Int) extends Ordered[Task]{

  private val nodes = mutable.Set[(Node,DAG)]()
  @volatile
  private[controller] var status = TaskState.WAIT
  private[controller] val createTime = System.currentTimeMillis()
  private[controller] var tryCount = 0

  val env = mutable.Map[String,Any]()
  val out = mutable.Map[String,Any]()
  var workId:String = _
  var submitTime:Long = 0
  private[controller] var acked = false

  def getDagNodes = nodes.toSeq

  def addDagNode(dag:DAG, node:Node): Unit = {
    val it = (node, dag)
    nodes += it
  }

  def removeDagNode(node:Node, dag:DAG) = {

    val it = (node, dag)
      nodes -= it

  }

  override def compare(that: Task): Int = {
    if(this.priority == that.priority){
      this.id - that.id
    }
    else {
      this.priority - that.priority
    }
  }
}


object TaskState extends Enumeration {

  type TaskState = Value

  val WAIT, PEND, RUN , TRY, COMPLETE = Value
}