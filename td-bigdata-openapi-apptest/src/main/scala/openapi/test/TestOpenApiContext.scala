package openapi.test

import openapi.OpenApiContext

/**
  * Created by Administrator on 2016/3/15.
  */
class TestOpenApiContext {

}

object TestOpenApiContext{
  def main(args: Array[String]) {
    val oc = new OpenApiContext("openapi://127.0.0.1:7077","routeApp")
  }
}
