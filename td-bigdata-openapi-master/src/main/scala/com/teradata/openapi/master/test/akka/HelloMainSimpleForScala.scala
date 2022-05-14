package com.teradata.openapi.master.test.akka

import akka.actor.{Props, ActorRef, ActorSystem}
import com.teradata.openapi.master.test.models.Animal
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
  * Created by Administrator on 2016/3/28.
  */
object HelloMainSimpleForScala {

  def main(args: Array[String]) {

      val ctx = new ClassPathXmlApplicationContext("config/spring/applicationContext-master.xml")
      //val ctx = new AnnotationConfigApplicationContext()
      val dog = ctx.getBean("dog").asInstanceOf[Animal]
      dog.speak
      val _system = ActorSystem("HelloForScala")
      val helloWorldActorRef:ActorRef = _system.actorOf(Props[HelloWorldActor])
      println("HelloWorld Actor path:"+helloWorldActorRef.path)

  }

}
