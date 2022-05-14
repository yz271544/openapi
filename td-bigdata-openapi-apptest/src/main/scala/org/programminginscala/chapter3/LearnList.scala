package org.programminginscala.chapter3

/**
  * Created by Administrator on 2016/3/12.
  */
object LearnList {

  def main(args: Array[String]) {

    val oneTwo = List(1,2)
    val threeFour = List(3,4)
    val oneTwoThreeFour = oneTwo ::: threeFour
    println("" + oneTwo + " and " + threeFour + " were not mutated.")
    println("Thus," + oneTwoThreeFour + "is a new List.")

    /** :: 操作使用*/
    val oneTwoThree = 1 :: 2 :: 3 :: Nil
    println(oneTwoThree)

  }

}
