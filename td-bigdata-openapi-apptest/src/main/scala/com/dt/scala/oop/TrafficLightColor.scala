package com.dt.scala.oop

/**
  * Created by Administrator on 2016/3/27.
  *
  * Scala和Java在枚举方面的区别是，scala枚举内部有一个Value类，所谓的枚举值都是通过它产生的
  * 不做任何约定的话，枚举值默认从0开始，依次+1
  */
object TrafficLightColor extends Enumeration{

  //val Red,Yellow,Green = Value

  val Red = Value("Stop")
  val Yellow = Value("hurry up")
  val Green = Value("Go")

  /*type TrafficLightColor = Value

  def doWhat(color:TrafficLightColor) = {

    if(color == Red) "Stop"
    else if (color == Yellow) "hurry up"
    else "go"

  }*/


  def main(args: Array[String]) {

    //枚举值的ID可通过id方法返回，名称通过toString方法返回
    println("---"+TrafficLightColor.Red.id)
    println("---"+TrafficLightColor.Red)

    //输出所有枚举值的集
    for(c <- TrafficLightColor.values) println(c.id + ": " + c)

    //通过枚举的ID或名称来进行查找定位
    println(TrafficLightColor(0))
    println(TrafficLightColor.withName("Stop").id)
  }

}
