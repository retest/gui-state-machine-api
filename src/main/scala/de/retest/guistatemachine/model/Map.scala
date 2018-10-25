package de.retest.guistatemachine.model

/**
 * This custom type allows storing values using [[Id]] as key.
 * [[JsonFormatForIdMap]] implements marshalling and unmarshalling for JSON for this type.
 * We cannot extend immutable maps in Scala, so we have to keep it as field.
 */
class Map[T](var values: scala.collection.immutable.Map[Id, T]) {
  /**
   * Generates a new ID based on the existing entries.
   * TODO Generate IDs in a better way. Maybe random numbers until one unused element is found?
   */
  def generateId: Id = this.synchronized { if (values.isEmpty) Id(0) else Id(values.keySet.max.id + 1) }

  def setValues(v: scala.collection.immutable.Map[Id, T]) { values = v }
  def getValues: scala.collection.immutable.Map[Id, T] = values
}