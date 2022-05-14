package typedActorExample.calculator

import akka.actor.TypedActor.Receiver

import scala.concurrent.Future


/**
  * Created by Administrator on 2016/3/18.
  */
trait CalculatorInt extends Receiver{

  def add(first:Int,second:Int): Future[Int]

  /**
    * 请求应答模式(request-reply manner similar to the ask())，是non-blocking
    *
    * @param first
    * @param second
    * @return
    */
  def subtract(first:Int,second:Int):Future[Int]

  def incrementCount():Unit

  /**
    * 请求应答模式，是blocking
    *
    * @return
    */
  def incrementAndReturn():Option[Int]

}
