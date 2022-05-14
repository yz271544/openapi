package com.dt.scala.oop

import scala.io.Source

/**
  * Created by Administrator on 2016/3/27.
  */
object ReadCharsTest {

  def main(args: Array[String]) {

    val source = Source.fromFile("D:\\aaa.txt","UTF-8")

    /** 如果想查看某个字符但又不处理掉它的话，调用source对象的buffered方法*/

    /*val iter = source.buffered
    while(iter.hasNext){
      if(!iter.head.equals("e"))
        print(iter.next)
      else
        print("%%")
    }*/

    /**读取源文件中所有以空格隔开的词法单元*/
    val tokens = source.mkString.split(" ")
    val numbers = for(w <- tokens) yield w.toDouble

    for(num <- numbers)
      println(num)

    /**scala中没有访问二进制文件的方法，需要使用Java中的类*/
    source.close
  }

}
