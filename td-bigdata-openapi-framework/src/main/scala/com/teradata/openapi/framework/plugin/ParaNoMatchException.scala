package com.teradata.openapi.framework.plugin

/**
  * Created by hdfs on 2016/7/13.
  */
private[openapi] class ParaNoMatchException(message: String, cause: Throwable) extends RuntimeException(message, cause){

	def this(message: String)= this(message, null)
	def this(cause: Throwable)= this(null, cause)
	def this() = this(null, null)
}

/*
object ParaNoMatchException extends App {
	val a = List(1)
	val b = List(2)

	val columnMap = Map("abc" -> a, "d" -> b)
	println(columnMap)

	if(columnMap.size == 1 && columnMap.keys.head.equals("abc")) {
		println("ok")
	} else {
		throw new ParaNoMatchException("No match")
	}

}*/
