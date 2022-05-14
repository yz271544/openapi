package com.teradata.openapi.master.test

import com.teradata.openapi.master.test.models.Animal
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
  * Created by Administrator on 2016/3/28.
  */
object ScalaSpringExample {

  def main(args: Array[String]) {
     val ctx = new ClassPathXmlApplicationContext("config/spring/applicationContext-master.xml")

    val dog = ctx.getBean("dog").asInstanceOf[Animal]
    val cat = ctx.getBean("cat").asInstanceOf[Animal]

    dog.speak
    cat.speak
  }

}
