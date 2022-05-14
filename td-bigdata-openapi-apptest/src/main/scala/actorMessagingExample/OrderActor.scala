package actorMessagingExample

import akka.actor.Actor

/**
  * Created by Administrator on 2016/3/17.
  */
class OrderActor extends Actor{


  override def receive = {
    case userId: Int =>
      sender ! new Order(userId,123,345,5)
  }
}
