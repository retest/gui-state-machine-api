package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id

import scala.collection.mutable.HashMap

/**
  * This custom type allows storing values using [[Id]] as key. We cannot extend immutable maps in Scala, so we have to
  * keep it as field. The implementation is thread-safe.
  */
@SerialVersionUID(1L)
case class IdMap[T]() extends Serializable {
  private type HashMapType = HashMap[Id, T]

  private val values = new HashMapType

  def addNewElement(v: T): Id = this.synchronized {
    val generatedId = generateId()
    values += (generatedId -> v)
    generatedId
  }

  def removeElement(id: Id): Boolean = this.synchronized {
    if (values.contains(id)) {
      values -= id
      true
    } else {
      false
    }
  }

  def getElement(id: Id): Option[T] = this.synchronized { values.get(id) }

  def hasElement(id: Id): Boolean = this.synchronized { values.contains(id) }

  def clear(): Unit = this.synchronized { values.clear() }

  def size: Int = this.synchronized { values.size }

  /**
    * Generates a new ID based on the existing entries.
    */
  private def generateId(): Id = {
    var id = Id(0L)
    while (values.keySet.contains(id)) { id = Id(id.id + 1) }
    id
  }
}

object IdMap {

  /**
    * Creates a new `IdMap` by a number of values.
    * @param v The initial values of the `IdMap`.
    * @tparam T The type of the values of the `IdMap`.
    * @return A newly created `IdMap`.
    */
  def apply[T](v: T*): IdMap[T] = {
    val r = new IdMap[T]()
    for (e <- v) { r.addNewElement(e) }
    r
  }
}
