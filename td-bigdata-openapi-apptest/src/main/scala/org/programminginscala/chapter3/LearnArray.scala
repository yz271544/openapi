package org.programminginscala.chapter3

import java.math.BigInteger

/**
  * Created by Administrator on 2016/3/12.
  */
object LearnArray {

  def main(args: Array[String]) {

    //val big = new BigInteger("12345")

    /*val greetStrings = new Array[String](3)
    greetStrings(0) = "Hello"
    greetStrings(1) = ","
    greetStrings(2) = "world!\n"*/

    val greetStrings = Array("Hello",",","world!")

    for(i <- 0 to 2)
      print(greetStrings(i))


  }

}
