package com.teradata.openapi.framework.util

import com.teradata.openapi.framework.OpenApiLogging

/**
  * Created by Administrator on 2016/4/1.
  */
private[openapi] object OpenApiUncaughtExceptionHandler
extends Thread.UncaughtExceptionHandler with OpenApiLogging{

  override def uncaughtException(thread: Thread, exception: Throwable) {
    try {
      logError("Uncaught exception in thread " + thread, exception)

      // We may have been called from a shutdown hook. If so, we must not call System.exit().
      // (If we do, we will deadlock.)
      if (!Utils.inShutdown()) {
        if (exception.isInstanceOf[OutOfMemoryError]) {
          System.exit(OpenApiExitCode.OOM)
        } else {
          System.exit(OpenApiExitCode.UNCAUGHT_EXCEPTION)
        }
      }
    } catch {
      case oom: OutOfMemoryError => Runtime.getRuntime.halt(OpenApiExitCode.OOM)
      case t: Throwable => Runtime.getRuntime.halt(OpenApiExitCode.UNCAUGHT_EXCEPTION_TWICE)
    }
  }

  def uncaughtException(exception: Throwable) {
    uncaughtException(Thread.currentThread(), exception)
  }

}
