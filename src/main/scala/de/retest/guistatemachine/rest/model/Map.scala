package de.retest.guistatemachine.rest.model

import scala.collection.immutable.HashMap

/**
  * This custom type allows storing values using [[Id]] as key.
  * [[de.retest.guistatemachine.rest.JsonFormatForIdMap]] implements marshalling and unmarshalling for JSON for this type.
  * We cannot extend immutable maps in Scala, so we have to keep it as field.
  */
case class Map[T](var values: scala.collection.immutable.Map[Id, T] = new HashMap[Id, T]) {

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

  def getElement(id: Id): T = values(id)

  def hasElement(id: Id): Boolean = values.contains(id)
}

object Map {
  def fromValues[T](v: T*): Map[T] = {
    val r = Map[T]()
    for (e <- v) r.addNewElement(e)
    r
  }
}