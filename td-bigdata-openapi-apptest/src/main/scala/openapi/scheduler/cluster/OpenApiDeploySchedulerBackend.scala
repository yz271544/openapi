package openapi.scheduler.cluster

import openapi.deploy.{ApplicationDescription, Command}
import openapi.deploy.client.{ClientListener, Client}
import openapi.{OpenApiContext, Logging}

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class OpenApiDeploySchedulerBackend(
       scheduler:ClusterScheduler,
       oc:OpenApiContext,
       master:String,
       appName:String)
  extends StandaloneSchedulerBackend(scheduler,oc.env.actorSystem)
  with ClientListener
  with Logging{

  var client: Client = null
  var stopping = false

  override def start(){

    super.start()

    val driverUrl = "akka://openapi@%s:%s/user/%s".format(
      System.getProperty("openapi.driver.host"), System.getProperty("openapi.driver.port"),
      StandaloneSchedulerBackend.ACTOR_NAME)

    logInfo("driver发送过来的url " + driverUrl)
    val args = Seq(driverUrl, "{{EXECUTOR_ID}}", "{{HOSTNAME}}")
    val command = Command("openapi.executor.StandaloneExecutorBackend",args,oc.executorEnvs)
    val appDesc = new ApplicationDescription(appName,command)
    client = new Client(oc.env.actorSystem,master,appDesc,this)
    client.start()
  }

  override def connected(appId: String) {
    logInfo("Connected to Spark cluster with app ID " + appId)
  }

  override def disconnected() {
    if (!stopping) {
      logError("Disconnected from Spark cluster!")
      //scheduler.error("Disconnected from Spark cluster")
    }
  }


}

























