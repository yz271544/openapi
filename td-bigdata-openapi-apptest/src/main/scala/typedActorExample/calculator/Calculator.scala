package typedActorExample.calculator

import akka.actor.{SupervisorStrategy, ActorRef, TypedActor}
import akka.event.Logging

import scala.concurrent.{Future, Promise}
import akka.actor.TypedActor.{Supervisor, PostStop, PreStart, context}

/**
  * Created by Administrator on 2016/3/18.
  */
class Calculator extends CalculatorInt with PreStart with PostStop with Supervisor{


  var counter:Int = 0
  val log = Logging(context.system,TypedActor.self.getClass)

  /**
    * Non blocking request response
    *
    * @param first
    * @param second
    * @return
    */
  //override def add(first: Int, second: Int): Future[Int] = Promise.successful(first)+second
  override def add(first: Int, second: Int): Future[Int] = {
    //val result = Promise[Int]
    //result.success(first+second).future
    Promise.successful(first + second).future
  }
  /**
    * 请求应答模式(request-reply manner similar to the ask())，是non-blocking
    *
    * @param first
    * @param second
    * @return
    */
  override def subtract(first: Int, second: Int): Future[Int] = {
    Promise.successful(first - second).future
  }
  override def incrementCount(): Unit = counter +=1

  /**
    * 请求应答模式，是blocking
    *
    * @return
    */
  override def incrementAndReturn(): Option[Int] = {
    counter += 1
    Some(counter)
  }


  override def onReceive(message: Any, sender: ActorRef): Unit = {
        log.info("Message received -> {}",message)
  }

  override def preStart():Unit ={
    log.info("Actor Started")
  }

  override def postStop():Unit={
    log.info("Actor Stopped")
  }

  override def supervisorStrategy(): SupervisorStrategy = ???
}
