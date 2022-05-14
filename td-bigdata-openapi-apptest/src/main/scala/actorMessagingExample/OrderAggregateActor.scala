package actorMessagingExample

import akka.actor.Actor

/**
  * Created by Administrator on 2016/3/17.
  */
class OrderAggregateActor extends Actor{
  override def receive = {
    case orderHistory: OrderHistory =>
      println(orderHistory.toString)
  }

}
