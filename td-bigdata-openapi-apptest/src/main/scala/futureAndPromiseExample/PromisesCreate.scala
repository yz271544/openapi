package futureAndPromiseExample

import openapi.Logging

import scala.concurrent.Promise
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 2016/3/19.
  */
object PromisesCreate extends Logging{

  def main(args: Array[String]) {
    val p = Promise[String]
    val q = Promise[String]

    p.future.foreach{
      case x => logInfo(s"p succeeded with '$x")
    }

    Thread.sleep(2000)
    p.success("assigned")


    q.failure(new Exception("not kept"))
    q.future.failed.foreach{
      case t => logInfo(s"q failed with $t")
    }

    Thread.sleep(1000)

  }

}
