package actorMessagingExample

import akka.actor.{Props, Actor}
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import akka.pattern.ask
import akka.pattern.pipe
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.{Await, Future}

/**
  * Created by Administrator on 2016/3/17.
  */
class ProcessOrderActor extends Actor{

  implicit val timeout = Timeout(5,TimeUnit.SECONDS)
  //implicit val timeout = Timeout(5 seconds)
  val orderActor = context.actorOf(Props[OrderActor])
  val addressActor = context.actorOf(Props[AddressActor])
  val orderAggregateActor = context.actorOf(Props[OrderAggregateActor])

  override def receive = {
    case userId: Integer =>
      val aggResult: Future[OrderHistory] =
        for{
          order <- ask(orderActor,userId).mapTo[Order]
          address <- addressActor ask(userId) mapTo manifest[Address]

          //address <- ask(addressActor,userId).mapTo[Address]

        } yield new OrderHistory(order,address)
      aggResult pipeTo orderAggregateActor
  }

}
