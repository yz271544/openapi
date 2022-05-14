package openapi


import openapi.scheduler.TaskScheduler
import openapi.scheduler.cluster.{OpenApiDeploySchedulerBackend, ClusterScheduler}
import openapi.scheduler.local.LocalScheduler
import scala.collection.mutable.HashMap
import scala.collection.{mutable, Map}

/**
  * Created by Administrator on 2016/3/14.
  */
class OpenApiContext(
      val master: String,
      val appName: String,
      val environment: Map[String, String] = Map())
    extends Logging{

  System.setProperty("openapi.driver.host","127.0.0.1")
  System.setProperty("openapi.driver.port","7077")
  System.setProperty("user.name","<unknown>")
  System.setProperty("user.password","<unknown>")


  //1. initialized logging

  //2. set driver host and port system properties

  //3. creat openapi execution environment
  private[openapi] val env = OpenApiEnv.createFromSystemProperties(
    "<driver>",
    System.getProperty("openapi.driver.host"),
    System.getProperty("openapi.driver.port").toInt,
    true,
    true)

  OpenApiEnv.set(env)

  private[openapi] val executorEnvs = HashMap[String, String]()

  //4.create and start the scheduler

  private var taskScheduler: TaskScheduler = {

    val LOCAL_N_REGEX = """local\[([0-9]+)\]""".r
    val SPARK_REGEX = """(openapi://.*)""".r

    master match {
      case "local" =>
        new LocalScheduler(1,0,this)

      case SPARK_REGEX(openapiUrl) =>
        logInfo("openapi url is ==>" + openapiUrl)
        val scheduler = new ClusterScheduler(this)
        val backend = new OpenApiDeploySchedulerBackend(scheduler,this,openapiUrl,appName)
        scheduler.initialize(backend)
        scheduler
    }
  }

  taskScheduler.start()



}























