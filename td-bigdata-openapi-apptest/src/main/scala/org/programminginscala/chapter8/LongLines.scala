package org.programminginscala.chapter8

import java.io.File

import scala.io.Source

/**
  * Created by Administrator on 2016/3/25.
  */
object LongLines {

  def processFile(filename:String,width:Int): Unit ={
    val source = Source.fromURL(filename,"utf-8")
    for(line <- source.getLines())
      processLine(filename,width,line)
  }

  private def processLine(filename:String,width:Int,line:String): Unit ={
    if(line.length > width)
      println(filename+": "+line.trim)
  }

  def subdirs(dir:File):Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs _)
  }

  def main(args: Array[String]) {
    val f:File = new File("D:\\test")
    for(d <- subdirs(f)){
      d.getName.split("：")(0).length match{
        case 3 => println(d.getName)
        case _ => println("aaaaa")
      }
    }

  }
}
