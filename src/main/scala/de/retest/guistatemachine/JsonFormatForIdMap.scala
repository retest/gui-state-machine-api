package de.retest.guistatemachine

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.Map
import scala.collection.immutable.HashMap

/**
 * Transforms a [[Map]] into a `scala.collection.immutable.Map[String, T]`, so it can be converted into valid JSON.
 * Besides, transforms a JSON object which is a `scala.collection.immutable.Map[String, T]` back into a [[Map]].
 * This transformer requires a JSON format for the type `K`.
 */
class JsonFormatForIdMap[T](implicit val jsonFormat: JsonFormat[T]) extends RootJsonFormat[Map[T]] {
  override def write(obj: Map[T]): JsValue =
    obj.values.map { field => (field._1.id.toString -> field._2) }.toJson

  override def read(json: JsValue): Map[T] = {
    val obj = json.asJsObject
    if (obj.fields.isEmpty) {
      new Map[T](new HashMap[Id, T]())
      // TODO Fix conversation back into the Map type.
    } else {
      val map = json.asInstanceOf[scala.collection.immutable.Map[String, T]]
      new Map[T](map.map { x => (Id(x._1.toLong) -> x._2) })
    }
  }
}