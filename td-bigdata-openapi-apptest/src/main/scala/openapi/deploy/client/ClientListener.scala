package openapi.deploy.client

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] trait ClientListener {

  def connected(appId: String): Unit

  def disconnected(): Unit

  //def executorAdded(fullId: String, workerId: String, host: String, cores: Int, memory: Int): Unit

  //def executorRemoved(fullId: String, message: String, exitStatus: Option[Int]): Unit

}
