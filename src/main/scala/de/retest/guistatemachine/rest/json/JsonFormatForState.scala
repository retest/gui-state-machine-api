package de.retest.guistatemachine.rest.json

import de.retest.guistatemachine.api.State
import de.retest.guistatemachine.api.impl.StateImpl
import spray.json.{JsObject, JsValue, RootJsonFormat}

class JsonFormatForState extends RootJsonFormat[State] {

  // TODO #1 Convert a State into JSON
  override def write(obj: State): JsValue = JsObject()

  // TODO #1 Convert JSON into State
  override def read(json: JsValue): State = throw new RuntimeException("Implementation is missing!")
}
