package com.teradata.openapi.framework.dao.jpa

import javax.persistence.{PersistenceContext, EntityManager}
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._
import scala.reflect._

/**
  * Created by Evan on 2016/5/4.
  */
@Transactional
class AbstractScalaJpaDaoSupport[T](val persistentClass:Class[T]) extends GenericDao[T]{

  @PersistenceContext
  var entityManager:EntityManager = _


  override def findAll()(implicit ct: ClassTag[T]): List[T] = {
    entityManager.createQuery("FROM " + persistentClass.getName,ct.runtimeClass).getResultList.toList.asInstanceOf[List[T]]
  }

  override def findById(id: Serializable)(implicit ct: ClassTag[T]): T = {
    entityManager.find(ct.runtimeClass,id).asInstanceOf[T]
  }

  override def remove(entity: T)(implicit ct: ClassTag[T]): Unit = {
    entityManager.remove(entity)
  }

  override def save(entity: T)(implicit ct: ClassTag[T]): T = {
    entityManager.persist(entity)
    entity
  }
}
