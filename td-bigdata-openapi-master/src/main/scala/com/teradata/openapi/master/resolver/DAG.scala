package com.teradata.openapi.master.resolver

import java.util.concurrent.atomic.AtomicInteger

import com.teradata.openapi.framework.deploy.DeployMessage
import com.teradata.openapi.framework.step.Step

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Created by lzf on 2016/3/31.
  */
class DAG (val source:DeployMessage){

  private val nodes = mutable.TreeSet[Node]()
  private val nextNodeId = new AtomicInteger(0)
  private val remainNodes = mutable.TreeSet[Node]()

  /*
  初始化复制节点
   */
  def init(): Unit = {
    remainNodes.clear()
    this.nodes.toSeq.foreach(node=>{
      remainNodes += node.clone()
    })
  }

  /*
  从剩余节点中获取可处理节点即入度为0的节点
   */
  def getRemainNodes: Seq[Node] = {
    remainNodes.filter(_.in.isEmpty).toSeq
  }

  def isOver = remainNodes.isEmpty

  def getNodes = this.nodes.toSeq

  def removeRemainNode(node:Node): Boolean = {

    val exist = this.remainNodes.contains(node)
    if(exist) {
      remainNodes -= node
      remainNodes.foreach(n => {
        n.in -= node.id
        n.out -= node.id
      })
    }
    exist
  }

  def createNode(step: Step): Node = {

    val node = new Node(nextNodeId.incrementAndGet(), step)
    nodes += node
    node
  }

  def deleteNode(node: Node): Boolean = {
    val exist = this.nodes.contains(node)
    if(exist) {
      nodes -= node
      nodes.foreach(n => {
        n.in -= node.id
        n.out -= node.id
      })
    }
    exist
  }

  //n1-->n2
  def addLink(n1: Node, n2: Node): Boolean = {
    val exist = this.nodes.contains(n1) && this.nodes.contains(n2)
    if(exist) {
      n1.addOut(n2)
      n2.addIn(n1)
    }
    exist
  }

  /*
  def nodeInSet(node: Node):List[Node] = {
    node.in.toList
  }

  def nodeOutSet(node: Node):List[Node] = {
    node.out.toList
  }*/

  def toJsonString: String = {

    ""
  }

  /*
  DAG 执行计划描述 并检测环路
   */
  def explain():String = {

    @tailrec
    def loop(in: mutable.ArrayBuffer[Node], no:Int=1, des:String=""):String = {
      if(in.isEmpty) return des
      val out = in.filter(_.in.isEmpty)
      if(out.isEmpty)
        throw new RuntimeException("has loops")
      in --= out
      in.foreach(_.in --= out.map(_.id))
      loop(in, no+1, des + s"step $no: ${out.map(_.step).mkString(", ")} \n")
    }

    val tmp = mutable.ArrayBuffer[Node]()
    this.nodes.toSeq.foreach(node=>{
      tmp += node.clone()
    })
    loop(tmp)

  }

}

class Node(val id: Int, val step: Step) extends Ordered[Node]
  with  mutable.Cloneable[Node]{

  private[resolver] var in = mutable.Set[Int]()
  private[resolver] var out = mutable.Set[Int]()

  private[resolver] def addIn(node: Node): Unit = {
    in += node.id
  }

  private[resolver] def removeIn(node: Node): Unit = {
    in -= node.id
  }

  private[resolver] def addOut(node: Node): Unit = {
    out += node.id
  }

  override def toString: String ={
    "id:"+id + " "+this.step

  }

  override def clone(): Node = {
    val n = new Node(id, step)

    n.in = this.in.clone()
    n.out = this.out.clone()

    //n.out = Set[Int](this.out.toSeq: _*)
    //n.in = Set[Int](this.in.toSeq: _*)
    n
  }

  override def compare(that: Node): Int = {
    this.id - that.id
  }
}

object DAG{

  def main(args: Array[String]) {
    val d = new DAG(null)

    val n1 = d.createNode(null)
    val n2 = d.createNode(null)
   // d.addLink(n1, n2)

    //d.deleteNode(n2)

    println(d.explain())
    println(d.explain())



  }

}
