package com.teradata.openapi.master.deploy


import scala.reflect.ClassTag
/**
  * Created by Evan on 2016/4/20.
  */
trait PersistenceEngine {

  /**
    * Defines how the object is serialized and persisted. Implementation will
    * depend on the store used.
    */
  def persist(name: String, obj: Object)

  /**
    * Defines how the object referred by its name is removed from the store.
    */
  def unpersist(name: String)

  /**
    * Gives all objects, matching a prefix. This defines how objects are
    * read/deserialized back.
    */
  def read[T: ClassTag](prefix: String): Seq[T]

  /*final def addApplication(app: ApplicationInfo): Unit = {
    persist("app_" + app.id, app)
  }

  final def removeApplication(app: ApplicationInfo): Unit = {
    unpersist("app_" + app.id)
  }*/

  final def addWorker(worker: WorkerInfo): Unit = {
    persist("worker_" + worker.id, worker)
  }

  final def removeWorker(worker: WorkerInfo): Unit = {
    unpersist("worker_" + worker.id)
  }

  /*final def addDriver(driver: DriverInfo): Unit = {
    persist("driver_" + driver.id, driver)
  }

  final def removeDriver(driver: DriverInfo): Unit = {
    unpersist("driver_" + driver.id)
  }*/

  /**
    * Returns the persisted data sorted by their respective ids (which implies that they're
    * sorted by time of creation).
    */
  /*final def readPersistedData(): (Seq[ApplicationInfo], Seq[DriverInfo], Seq[WorkerInfo]) = {
    (read[ApplicationInfo]("app_"), read[DriverInfo]("driver_"), read[WorkerInfo]("worker_"))
  }*/

  def close() {}

}

private[openapi] class BlackHolePersistenceEngine extends PersistenceEngine {

  override def persist(name: String, obj: Object): Unit = {}

  override def unpersist(name: String): Unit = {}

  override def read[T: ClassTag](name: String): Seq[T] = Nil

}
