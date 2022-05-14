package actorMessagingExample

import akka.actor.Actor

/**
  * Created by Administrator on 2016/3/17.
  */
class AddressActor extends Actor{

  override def receive = {
    case userId: Int =>
      sender ! new Address(userId,"Munish Gupta", "Sarjapura Road", "Bangalore, India")
  }
}
