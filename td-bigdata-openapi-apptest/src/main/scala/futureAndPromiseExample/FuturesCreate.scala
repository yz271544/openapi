package futureAndPromiseExample

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future}

/**
  * Created by Administrator on 2016/3/18.
  */
object FuturesCreate{

  def main(args: Array[String]) {
    Future{
      println("the future is here!")
    }
    println("the future is coming")
    Thread.sleep(1000)
  }

}
