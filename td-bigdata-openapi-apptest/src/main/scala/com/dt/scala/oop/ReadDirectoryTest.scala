package com.dt.scala.oop

import java.io.File
import java.nio.file.{SimpleFileVisitor, Path}

/**
  * Created by Administrator on 2016/3/28.
  */
object ReadDirectoryTest {

  def subdirs(dir:File):Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs)
  }

  /*implicit def makeFileVisitor(f:(Path) => Unit) = new SimpleFileVisitor[Path]{
     override def visitFile(p:Path,attrs:attribute)
  }*/

}
