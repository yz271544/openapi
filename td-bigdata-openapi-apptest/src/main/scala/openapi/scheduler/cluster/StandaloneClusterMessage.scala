package openapi.scheduler.cluster

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] sealed trait StandaloneClusterMessage extends Serializable

  //Driver to executors

  private[openapi] case class RegisteredExecutor(sparkProperties: Seq[(String, String)])
    extends StandaloneClusterMessage

  //Executors to driver
  private[openapi] case class RegisterExecutor(executorId:String,host: String)
     extends StandaloneClusterMessage


