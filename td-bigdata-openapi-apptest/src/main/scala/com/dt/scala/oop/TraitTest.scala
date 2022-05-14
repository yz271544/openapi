package com.dt.scala.oop

/**
  * Created by Administrator on 2016/3/28.
  */
object TraitTest {

}

trait Logger{
  /**特质中不需要将方法声明为abstract -- 特质中未被实现的方法默认就是抽象的*/
  def log(msg:String)
}

/**所有接口都可以作为Scala特质使用*/
class ConsoleLogger extends Logger with Cloneable with Serializable{
  /**在重写特质的抽象方法时不需要给出override关键字*/
  def log(msg:String): Unit ={
    println(msg)
  }

  /***/
}
