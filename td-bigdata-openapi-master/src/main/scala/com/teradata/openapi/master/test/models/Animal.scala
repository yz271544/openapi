package com.teradata.openapi.master.test.models

//import javax.persistence.Entity

import scala.beans.BeanProperty

/**
  * Created by Administrator on 2016/3/28.
  */
abstract class Animal(name:String) {

  def speak:Unit

}

 //@Entity(name = "dog")
class Dog(@BeanProperty var name:String) extends Animal(name){

  override def speak: Unit ={
    println(name + " says woof")
  }
}

//@Entity(name = "cat")
class Cat(var name:String) extends Animal(name){

  override def speak: Unit ={
    println(name + " says Meow")
  }
}
