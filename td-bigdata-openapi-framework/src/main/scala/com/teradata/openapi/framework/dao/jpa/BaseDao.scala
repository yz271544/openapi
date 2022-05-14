package com.teradata.openapi.framework.dao.jpa

import javax.persistence.EntityManager

import org.hibernate.Session

/**
  * Data Access Object (DAO) interface. This is an interface used to tag our DAO
  * classes and to provide common methods to all DAOs.
  * Created by Evan on 2016/5/3.
  */
trait BaseDao{
/*
  /** 通用`Entity`类型 */
  type Entity

  /** 通用的`Entity's Id` 类型 */
  type Id

  /**
    * Method to insert the new row into database table
    * @param obj The object entity to be persisted
    */
  def insert(obj: Entity):Unit

  /**
    * Method to update an existing row in the database table
    * @param obj The object entity to be updated
    */
  def update(obj: Entity):Unit

  /**
    * Method to insert a new row or update a row if it was
    * already existing in the system
    * @param obj The object entity to be updated
    */
  def insertOrUpdate(obj: Entity):Unit

  /**
    * Method to delete an existing row in the database table
    * @param obj The object entity to be deleted
    */
  def delete(obj: Entity):Unit

  /**
    * Method to find a database item by id
    * @param id The id by which the row has to be found
    * @param query The name of the NamedQuery to be executed
    * @return
    */
  def find(id: Id):Option[Entity]

  /**
    *
    * @param id
    * @return
    */
  def findByParameter(query:String,parameter:Any*):List[Entity]

  /**
    *
    * @return
    */
  def findAll(query:String):List[Entity]
*/

  //def getEntityManager():EntityManager

  //def getSession():Session

  //----------------QL Query--------------------
  //def find(qlString:String,parameter:Any*):List[T]

}
