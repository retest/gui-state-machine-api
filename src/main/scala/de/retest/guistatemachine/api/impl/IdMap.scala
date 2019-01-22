package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id

import scala.collection.immutable.HashMap

/**
  * This custom type allows storing values using [[Id]] as key.
  * We cannot extend immutable maps in Scala, so we have to keep it as field.
  */
@SerialVersionUID(1L)
case class IdMap[T]() extends Serializable {
  private type HashMapType = HashMap[Id, T]

  private var values = new HashMapType

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

  def size: Int = values.size

  /**
    * Generates a new ID based on the existing entries.
    */
  private def generateId: Id = {
    var id = Id(0L)
    while (values.keySet.contains(id)) { id = Id(id.id + 1) }
    id
  }
}

object IdMap {
  def apply[T](v: T*): IdMap[T] = {
    val r = new IdMap[T]()
    for (e <- v) { r.addNewElement(e) }
    r
  }
}
