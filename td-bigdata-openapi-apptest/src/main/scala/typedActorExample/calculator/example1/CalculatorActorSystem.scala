package typedActorExample.calculator.example1

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import typedActorExample.calculator.{CalculatorRef, Calculator, CalculatorInt}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import scala.concurrent.{Future, Await}

/**
  * Created by Administrator on 2016/3/21.
  */
object CalculatorActorSystem {

  def main(args: Array[String]) {

    def longRunningOperation(): Int = { Thread.sleep(1000); 1 }
    val f = Future { longRunningOperation() }


     val _system = ActorSystem("TypedActorsExample")
     val calculator:CalculatorInt = TypedActor(_system).typedActorOf(TypedProps[Calculator])

     calculator.incrementCount()
     implicit val timeout = Timeout(5,TimeUnit.SECONDS)

     val future = calculator.add(14,6)
     //val f2 = Future{future.asInstanceOf}
     //var result = Await.result(f2,timeout.duration)
     var result = Await.result(future,timeout.duration)
     println("Result is " + result)

     // Invoke the method and wait for result
     var counterResult = calculator.incrementAndReturn()
     println("Result is " + counterResult.get)
     counterResult = calculator.incrementAndReturn()
     println("Result is " + counterResult.get)

     //Get access to the ActorRef
     val calActor: ActorRef = TypedActor(_system).getActorRefFor(calculator)
     //val calActor: ActorRef = _system.actorOf(Props[CalculatorRef],"testActor")

     //call actor with a message
     calActor ! "hello"


    Thread.sleep(2000)
     _system.shutdown()
  }

}
