package com.dt.scala.oop

/**
  * Created by Administrator on 2016/3/26.
  */
class Teacher {

  //会生成公有的getter and setter 方法
  var name : String = _
  //生成的getter and setter也是私有的
  private var age = 27
  //gender只属于当前的实例
  private[this] val gender = "male"

  def this(name:String){
    this
    this.name = name
  }

  def sayHello(): Unit ={
    println(this.name + ":" + this.age + ":" + this.gender)
  }

}
