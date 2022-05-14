package com.teradata.openapi.master.test

import java.io.StringWriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * Created by John on 2016/8/10.
  */
object TestFastjackson {


  def main(args: Array[String]) {
    jacksonTest()
  }


  @throws[Exception] def jacksonTest(): Unit = {

    //case class Person(var name: String = "", var age: Int = 0)
    //case class Person(@Bean var name: String, @Bean var age: Int)


    val person = Person("fred", 25)
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    val out = new StringWriter
    mapper.writeValue(out, person)
    val json = out.toString()
    println(json)

    val person2 = mapper.readValue(json, classOf[Person])
    println(person2)
  }
}

case class Person( name: String, age: Int )