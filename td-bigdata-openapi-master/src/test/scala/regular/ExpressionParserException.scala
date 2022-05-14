package regular

class ExpressionParserException (msg: String) extends RuntimeException(msg) {
  def this() = {this(msg = "")}
}

