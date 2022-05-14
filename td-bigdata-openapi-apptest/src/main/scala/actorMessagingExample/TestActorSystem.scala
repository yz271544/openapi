package actorMessagingExample

import akka.actor.{Props, ActorSystem}

/**
  * Created by Administrator on 2016/3/17.
  */

case class Order(userId: Int, orderNo: Int, amount: Float, noOfItems: Int)
case class Address(userId: Int, fullName: String, address1: String, address2: String)
case class OrderHistory(order: Order, address: Address)

object TestActorSystem {

  def main(args: Array[String]) {

    val _system = ActorSystem("FutureUsageExample")
    val processOrder = _system.actorOf(Props[ProcessOrderActor])
    processOrder ! 456
    Thread.sleep(5000)
    _system.shutdown()

  }
}
