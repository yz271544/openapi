package com.teradata.openapi.framework.util.pluginUtil

/**
  * Created by zyx on 2016/7/18.
  */
import java.io.{File}
import java.net.{URI, URL}
import java.nio.charset.Charset
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributeView


import java.text.SimpleDateFormat


import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.io.Source

object PathHelper {
  val format = new SimpleDateFormat("yyyyMMdd HH:mm:ss")

  implicit def string2path(pathString: String): Path = Paths.get(pathString)

  implicit def uri2path(uri: URI): Path = Paths.get(uri)

  implicit def url2path(url: URL): Path = Paths.get(url.toURI)

  implicit def segments2path(segments: Seq[String]): Path = Paths.get(segments.head, segments.tail: _*)

  implicit class RichPath(val path: Path) extends AnyVal {

    def /(pathString: String) = path.resolve(pathString)

    def /(other: Path) = path.resolve(other)

    def filename = path.getFileName.toString

    def exists = Files.exists(path)

    def mkdirs = Files.createDirectories(path)

    def touch = Files.createFile(path)

    def delete() = Files.delete(path)

    def inputStream = Files.newInputStream(path)

    def outputStream = Files.newOutputStream(path)

    def isFile = Files.isRegularFile(path)

    def isDirectory = Files.isDirectory(path)

    def segments = path.iterator.toList

    def ancestor(n: Int): Path = {
      @tailrec
      def loop(path: Path, n: Int): Path =
        n match {
          case 0 => path
          case _ => loop(path.getParent, n - 1)
        }

      require(n >= 0, s"ancestor rank must be positive but asked for $n")
      require(n <= path.segments.length, s"can't ask for ancestor rank $n while segments length is ${path.segments.length}")
      loop(path, n)
    }

    def ifFile[T](f: File => T): Option[T] = if (isFile) Some(f(path.toFile)) else None

    def writer(charset: Charset) = Files.newBufferedWriter(path, charset)

    def copyTo(other: Path, options: CopyOption*) = Files.copy(path, other, options: _*)

    def extension: String = {
      val pathString = path.toString
      val dotIndex = pathString.lastIndexOf('.')
      if (dotIndex == -1) "" else pathString.substring(dotIndex + 1)
    }

    def hasExtension(ext: String, exts: String*) = {
      val lower = extension.toLowerCase
      ext.toLowerCase == lower || exts.exists(_.toLowerCase == lower)
    }

    def stripExtension = filename stripSuffix ("." + extension)

    def deepList(maxDepth: Int = -1): List[Path] = {
      @tailrec
      def deepListAux(current: Path, unvisited: List[Path], acc: List[Path], maxDepth: Int): List[Path] = {
        val entries = Files.newDirectoryStream(current).toList
        val toVisit = unvisited ++ entries.filter(p => p.isDirectory)
        if (toVisit.isEmpty || maxDepth == 0) acc ++ entries
        else deepListAux(toVisit.head, toVisit.tail, acc ++ entries, maxDepth - 1)
      }

      if (path.exists)
        deepListAux(path, Nil, Nil, maxDepth)
      else Nil
    }

    def deepFiles = deepList().filter(_.isFile)

    def deepDirs = deepList().filter(_.isDirectory)

    def files = deepList(maxDepth = 1).filter(_.isFile)
  }

  private[this] def getFileCheckContent(path:Path):String = {
    //Create file attribute object
    val basicfile = Files.getFileAttributeView(
        path, classOf[BasicFileAttributeView]
    ).readAttributes()
   //Create file read object
    val reader = Source.fromFile(path.toFile)

    List(
        path.toAbsolutePath.toString
      , basicfile.size()
      , reader.getLines().length
      , this.format.format(basicfile.lastModifiedTime())
      ).mkString("\t")
  }

  def getCheckContent(path:Path) = {
    val reList :List[String] = List()
    if (path.isDirectory) for (elem <- path.deepList( -1 )) {
	  getFileCheckContent(elem)
	} else if (path.isFile) {
      reList.+(this.getFileCheckContent(path))
    } else {
      println( "Error: The checkfileName is not fount!!!" )
    }
    reList
  }

}
