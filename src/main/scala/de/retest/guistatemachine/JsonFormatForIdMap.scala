package de.retest.guistatemachine

import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.Map
import spray.json.JsValue
import spray.json.JsonFormat
import spray.json.RootJsonFormat

/**
 * Transforms a [[Map]] into a `scala.collection.immutable.Map[String, T]`, so it can be converted into valid JSON.
 * Besides, transforms a JSON object which is a `scala.collection.immutable.Map[String, T]` back into a [[Map]].
 * This transformer requires a JSON format for the type `K`.
 */
class JsonFormatForIdMap[T](implicit val jsonFormat0: JsonFormat[scala.collection.immutable.Map[String, T]], implicit val jsonFormat1: JsonFormat[T]) extends RootJsonFormat[Map[T]] {
  override def write(obj: Map[T]): JsValue =
    jsonFormat0.write(obj.values.map { field => (field._1.id.toString -> field._2) })

  override def read(json: JsValue): Map[T] = {
    val map = jsonFormat0.read(json)
    new Map[T](map.map { x => (Id(x._1.toLong) -> x._2) })
  }
}