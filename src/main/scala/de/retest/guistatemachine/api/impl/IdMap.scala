package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id

import scala.collection.immutable.HashMap

/**
  * This custom type allows storing values using [[Id]] as key.
  * We cannot extend immutable maps in Scala, so we have to keep it as field.
  */
@SerialVersionUID(1L)
case class IdMap[T]() extends Serializable {
  var values = new HashMap[Id, T]

  /**
    * Generates a new ID based on the existing entries.
    * TODO #1 Generate IDs in a better way. Maybe random numbers until one unused element is found?
    */
  def generateId: Id = if (values.isEmpty) Id(0) else Id(values.keySet.max.id + 1)
  def addNewElement(v: T): Id = {
    val generatedId = generateId
    values = values + (generatedId -> v)
    generatedId
  }

  def removeElement(id: Id): Boolean =
    if (values.contains(id)) {
      values = values - id
      true
    } else {
      false
    }

  def getElement(id: Id): Option[T] = values.get(id)

  def hasElement(id: Id): Boolean = values.contains(id)

  def clear(): Unit = values = new HashMap[Id, T]
}

object IdMap {
  def apply[T](): IdMap[T] = new IdMap[T]

  def fromValues[T](v: T*): IdMap[T] = {
    val r = IdMap[T]()
    for (e <- v) r.addNewElement(e)
    r
  }
}
