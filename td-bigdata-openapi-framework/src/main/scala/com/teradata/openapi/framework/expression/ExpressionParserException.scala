package com.teradata.openapi.framework.expression

class ExpressionParserException (msg: String) extends RuntimeException(msg) {
  def this() = {this(msg = "")}
}

