package com.dt.scala.oop

import scala.io.Source

/**
  * Created by Administrator on 2016/3/27.
  */
object ReadLineTest {

  def main(args: Array[String]) {
    //第一个参数可以是字符串或者是java.io.File
    //如果知道文件使用的是当前平台缺省的字符编码，则可以略去第二个字符编码参数
    val source = Source.fromFile("D:\\aaa.txt","UTF-8")




    /**迭代循环*/
    /*
      val lineIterator = source.getLines
      for(l <- lineIterator)
      println(l)*/

    /**将这些行放到数组或数组缓冲中*/
    /*val lines = source.getLines.toArray
    for(l <- lines)
      println("--"+l)*/

    /**把整个文件读取成一个字符串*/
    val contents = source.mkString
    println(contents)
    source.close



  }



}
