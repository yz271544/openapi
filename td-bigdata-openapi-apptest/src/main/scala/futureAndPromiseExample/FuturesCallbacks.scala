package futureAndPromiseExample

import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 2016/3/18.
  */
object FuturesCallbacks {

  def main(args: Array[String]) {

    def getUrlSpec():Future[List[String]] = Future{

      //val url = "http://www.w3.org/Addressing/URL/url-spec.txt"
      //val f = Source.fromURL(url)
      //val f = Source.fromFile("D:/url-spec.txt")
      val f = Source.fromFile("D:/aaa.txt")
      try f.getLines.toList finally f.close
    }

    val urlSpec:Future[List[String]] = getUrlSpec()

    def find(lines:List[String],keyword:String):String =
    lines.zipWithIndex.collect{
      case (line,n) if line.contains(keyword) => (n,line)
    }.mkString("\n")

    //foreach接收偏函数作为函数参数
    urlSpec.foreach{
      case lines => println(find(lines,"telent"))
    }

    println("callback registered,continuing with other work")
    Thread.sleep(5000)

  }

}
