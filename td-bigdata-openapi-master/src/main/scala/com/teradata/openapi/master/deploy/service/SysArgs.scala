package com.teradata.openapi.master.deploy.service

import com.teradata.openapi.framework.model.ApiSysArgRow
import com.teradata.openapi.master.deploy.dao.ApiSysArgDao

/**
  * Created by John on 2016/7/15.
  */
class SysArgs {
  val apiSysArg: Seq[ApiSysArgRow] = ApiSysArgDao().load()
  val sysArgs: Map[String, String] = apiSysArg.map{ a => a.sys_arg_name -> a.sys_arg_val}.toMap
}
object SysArgs {
  def apply() = {
    new SysArgs()
  }
}

object SysArgsTest {
  def main(args: Array[String]) {
    val a = SysArgs()
    println(a.sysArgs)

  }
}