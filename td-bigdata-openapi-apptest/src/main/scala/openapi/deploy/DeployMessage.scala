package openapi.deploy

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] sealed trait DeployMessage extends Serializable

  //Client to Master
  private case class RegisterApplication(appDescription:ApplicationDescription)
    extends DeployMessage

  //Master to Client

  private case class RegisteredApplication(appId: String) extends DeployMessage
