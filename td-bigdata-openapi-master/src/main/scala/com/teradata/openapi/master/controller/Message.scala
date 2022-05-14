package com.teradata.openapi.master.controller

import com.teradata.openapi.framework.plugin.TaskPlugin
import com.teradata.openapi.framework.step.Step
import com.teradata.openapi.master.resolver.DAG

/**
  * Created by lzf on 2016/4/7.
  */
object Message {

  case class DAGExecutePlan(dag: DAG)

  case class DispatchTask(task: Task)

  case class DispatchClear(task: Task)

  case class SubmitTask(task: Task)

  case class TaskSubmitException(id:Int, e:Exception)

  case class Execute1(id: Int, plugin: TaskPlugin[Step])


}
