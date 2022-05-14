package com.teradata.openapi.framework.dao.jpa

import scala.reflect.ClassTag

/**
  * Created by Evan on 2016/5/4.
  */
trait GenericDao[T] {

  def findAll()(implicit ct: ClassTag[T]):List[T]

  def save(entity:T)(implicit ct: ClassTag[T]):T

  def remove(entity:T)(implicit ct: ClassTag[T]):Unit

  def findById(id:Serializable)(implicit ct: ClassTag[T]):T

}
