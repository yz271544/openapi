package com.teradata.openapi.master.controller

import akka.actor.{Actor, ActorRef}
import com.teradata.openapi.framework.OpenApiLogging

import com.teradata.openapi.master.controller.Message.{DispatchClear, DispatchTask, SubmitTask}


/**
  * Created by lzf on 2016/4/12.
  */
class PendActor(controller:Controller, queue:Strategy, submitActor: ActorRef) extends Actor with OpenApiLogging{

  override def receive: Receive = {

    case DispatchTask(task: Task) =>
      task.status = TaskState.PEND
      dispatch(task)
    case DispatchClear(task: Task) =>
      clear(task)

  }

  private def dispatch(task: Task): Unit = {
    this.queue.enter(task).distribute().foreach(submit)
  }

  private def clear(task: Task): Unit = {
    this.queue.clean(task).distribute().foreach(submit)
  }

  def submit(task:Task): Unit = {
    submitActor ! SubmitTask(task)
  }


}
