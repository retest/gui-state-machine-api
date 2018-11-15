package de.retest.guistatemachine.rest.json

import de.retest.guistatemachine.rest.GetStateBody
import spray.json.{JsObject, JsValue, RootJsonFormat}

class JsonFormatForGetStateBody extends RootJsonFormat[GetStateBody] {
  import de.retest.guistatemachine.rest.GetStateBody

  // TODO #1 Convert a GetStateBody into JSON
  override def write(obj: GetStateBody): JsValue = JsObject()

  // TODO #1 Convert JSON into GetStateBody
  override def read(json: JsValue): GetStateBody = throw new RuntimeException("Implementation is missing!")
}
