package com.teradata.openapi.master.deploy

import com.teradata.openapi.framework.{OpenApiLogging, OpenApiConf}
import com.teradata.openapi.framework.util.{IntParam, Utils}

/**
  * Created by Administrator on 2016/4/1.
  */
class MasterArguments(args: Array[String], conf:OpenApiConf){

  var host = Utils.localHostName()  //"127.0.0.1"
  var port = 8888

  if(System.getenv("OPENAPI_MASTER_HOST") != null){
    host = System.getenv("OPENAPI_MASTER_HOST")
  }

  if(System.getenv("OPENAPI_MASTER_PORT") != null){
    port = System.getenv("OPENAPI_MASTER_PORT").toInt
  }

  parse(args.toList)


  def parse(args: List[String]): Unit = args match {
    case ("--ip" | "-i") :: value :: tail =>
      Utils.checkHost(value, "ip no longer supported, please use hostname " + value)
      host = value
      parse(tail)

    case ("--host" | "-h") :: value :: tail =>
      Utils.checkHost(value, "Please use hostname " + value)
      host = value
      parse(tail)

    case ("--port" | "-p") :: IntParam(value) :: tail =>
      port = value
      parse(tail)


    case Nil => {println("---------无参数!!!!--------")}

    case _ =>
      printUsageAndExit(1)
  }


  def printUsageAndExit(exitCode: Int) {
    System.err.println(
      "Usage: Master [options]\n" +
        "\n" +
        "Options:\n" +
        "  -i HOST, --ip HOST     Hostname to listen on (deprecated, please use --host or -h) \n" +
        "  -h HOST, --host HOST   Hostname to listen on\n" +
        "  -p PORT, --port PORT   Port to listen on (default: 7077)\n" +
        "                         Default is conf/openapi-defaults.conf.")
    System.exit(exitCode)
  }

}
