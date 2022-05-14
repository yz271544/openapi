package com.dt.scala.oop

/**
  * Created by Administrator on 2016/3/26.
  */
class ApplyTest {

  def apply() = println("I am into Spark so much!!!!")

  def haveATry: Unit ={
    println("Have a try on apply!")
  }

}

object ApplyTest{
  def apply() = {
    println("I am into Scala so much!!!")
    new ApplyTest
  }
}

object ApplyOperation{
  def main(args: Array[String]) {
    //val array = Array(1,2,3,4,5)
    //val a = ApplyTest()
    //a.haveATry

    val b = new ApplyTest
    b.haveATry
    println(b())
  }
}


