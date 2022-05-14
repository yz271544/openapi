package futureAndPromiseExample

import java.io.File
import openapi.Logging
import org.apache.commons.io.FileUtils
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.convert.decorateAsScala._

/**
  * Created by Administrator on 2016/3/18.
  */
object FuturesClumsyCallback extends Logging{

  def main(args: Array[String]) {

    def blacklistFile(name:String):Future[List[String]] = Future{
      val lines = Source.fromFile(name).getLines
      lines.filter(x => !x.startsWith("#") && !x.isEmpty).toList
    }

    def findFiles(patterns:List[String]):List[String] = {
      val root = new File(".")
      for {
        f <- FileUtils.iterateFiles(root, null, true).asScala.toList
        pat <- patterns
        abspat = root.getCanonicalPath + File.separator + pat
        if f.getCanonicalPath.contains(abspat)
      } yield f.getCanonicalPath
    }

    println("------"+blacklistFile("D:\\.gitignore").asInstanceOf)

    blacklistFile("D:\\.gitignore").foreach{
      case lines =>
        val files = findFiles(lines)
        logInfo(s"matches: ${files.mkString("\n")}")
    }

    Thread.sleep(1000)

  }

}
