package com.teradata.openapi.framework.dao.jpa

//import javax.persistence.{TypedQuery, EntityManager}

import org.hibernate
import org.hibernate.{Query, Session, FlushMode}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConversions._

/**
  * Created by Evan on 2016/5/3.
  */
abstract class BaseHibernateDao extends BaseDao{
/*
  type T = Entity
  type Tid = Id


  /**
    * 获取实体工厂管理对象
    */
  @Autowired
  var entityManager:EntityManager = _

  /**
    * 获取Session
    *
    * @return
    */
  def getSession:Session = {

    entityManager.getDelegate.asInstanceOf[Session]

  }

  /**
    * 创建QL查询对象
    *
    * @param qlString
    * @param parameter
    * @return
    */
  def createQuery(sqlString:String,parameter:Any*):Query = {
    val query:Query = getSession.createQuery(sqlString)
    setParameter(query,parameter)
    query
  }

  /**
    * 设置查询参数
    *
    * @param query
    * @param parameter
    */
  private def setParameter(query:Query,parameter:Any*): Unit ={
    if(parameter != None){
      for(i <- 0 until parameter.length)
        query.setParameter(i,parameter(i))
    }

  }

  /*-------------------------QL-------------------------------*/

  /**
    * Method to insert the new row into database table
    *
    * @param obj The object entity to be persisted
    */
  override def insert(obj: Entity): Unit ={
    getSession.setFlushMode(FlushMode.AUTO)
    getSession.persist(obj)
    getSession.flush
  }

  /**
    * Method to update an existing row in the database table
    *
    * @param obj The object entity to be updated
    */
  override def update(obj: Entity): Unit = {
    getSession.setFlushMode(FlushMode.AUTO)
    getSession.merge(obj)
    getSession.flush()
  }

  /**
    * Method to insert a new row or update a row if it was
    * already existing in the system
    *
    * @param obj The object entity to be updated
    */
  override def insertOrUpdate(obj: Entity): Unit = {
    getSession.setFlushMode(FlushMode.AUTO)
    getSession.saveOrUpdate(obj)
    getSession.flush()
  }



  /**
    *
    * @param id
    * @return
    */
  override def findByParameter(query:String,parameter:Any*): List[Entity] = {
    createQuery(query,parameter).list().asInstanceOf[List[Entity]]
  }


  /**
    * Method to find a database item by id
    *
    * @param id    The id by which the row has to be found
    * @param query The name of the NamedQuery to be executed
    * @return
    */
  override def find(id: Id): Option[Entity] = {
    //Some("aaaa")
    Option(entityManager.find(classOf[Entity],id))
  }

  /**
    *
    * @return
    */
  override def findAll(query:String): List[Entity] = {
    //entityManager.createQuery(query,classOf[T]).getResultList.toList
    createQuery(query,None).list().asInstanceOf[List[Entity]]
  }

  /**
    * Method to delete an existing row in the database table
    *
    * @param obj The object entity to be deleted
    */
  override def delete(obj: Entity): Unit = {
    getSession.setFlushMode(FlushMode.AUTO)
    getSession.delete(obj)
    getSession.flush()
  }
*/

}
