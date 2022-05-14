package com.teradata.openapi.worker.deploy

import com.teradata.openapi.framework.OpenApiConf
import com.teradata.openapi.framework.util.{IntParam, Utils}

/**
  * Created by Evan on 2016/4/19.
  */
private[openapi] class WorkerArguments(args:Array[String],conf:OpenApiConf) {

  var host = Utils.localHostName()
  var port = 0
  var masters:Array[String] = null
  var workDir:String = null

  // Check for settings in environment variables
  if (System.getenv("OPENAPI_WORKER_PORT") != null) {
    port = System.getenv("OPENAPI_WORKER_PORT").toInt
  }

  if (System.getenv("OPENAPI_WORKER_DIR") != null) {
    workDir = System.getenv("OPENAPI_WORKER_DIR")
  }

  parse(args.toList)

  def parse(args:List[String]):Unit = args match{
    case ("--ip" | "-i") :: value :: tail =>
      Utils.checkHost(value,"ip no longer supported, please use hostname " + value)
      host = value
      parse(tail)

    case ("--host" | "-h") :: value :: tail =>
      Utils.checkHost(value, "Please use hostname " + value)
      host = value
      parse(tail)

    case ("--port" | "-p") :: IntParam(value) :: tail =>
      port = value
      parse(tail)

    case ("--work-dir" | "-d") :: value :: tail =>
      workDir = value
      parse(tail)

    case value :: tail =>
      if (masters != null) {  // Two positional arguments were given
        printUsageAndExit(1)
      }

      masters = value.stripPrefix("openapi://").split(",").map("openapi://" + _)
      println("-----master的URL-----" + value.stripPrefix("openapi://"))

      parse(tail)

    case Nil =>
      if (masters == null) {  // No positional argument was given
        printUsageAndExit(1)
      }

    case _ =>
      printUsageAndExit(1)


  }

  /**
    * Print usage and exit JVM with the given exit code.
    */
  def printUsageAndExit(exitCode: Int): Unit ={

    System.err.println(
      "Usage: Worker [options] <master>\n" +
        "\n" +
        "Master must be a URL of the form openapi://hostname:port\n" +
        "\n" +
        "Options:\n" +
        "  -d DIR, --work-dir DIR   Directory to run apps in (default: OPENAPI_HOME/work)\n" +
        "  -i HOST, --ip IP         Hostname to listen on (deprecated, please use --host or -h)\n" +
        "  -h HOST, --host HOST     Hostname to listen on\n" +
        "  -p PORT, --port PORT     Port to listen on (default: random)\n" +
        "                           Default is conf/openapi-defaults.conf.")
      System.exit(exitCode)

  }

}
