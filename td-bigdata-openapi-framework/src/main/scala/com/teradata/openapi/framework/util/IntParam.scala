package com.teradata.openapi.framework.util

/**
  * Created by Evan on 2016/4/19.
  */
private[openapi] object IntParam {

  def unapply(str: String): Option[Int] = {
    try {
      Some(str.toInt)
    } catch {
      case e: NumberFormatException => None
    }
  }

}
