package de.retest.guistatemachine.rest.json

import de.retest.guistatemachine.rest.ExecuteActionBody
import spray.json.{JsObject, JsValue, RootJsonFormat}

class JsonFormatForExecuteActionBody extends RootJsonFormat[ExecuteActionBody] {
  import de.retest.guistatemachine.rest.ExecuteActionBody

  // TODO #1 Convert a ExecuteActionBody into JSON
  override def write(obj: ExecuteActionBody): JsValue = JsObject()

  // TODO #1 Convert JSON into ExecuteActionBody
  override def read(json: JsValue): ExecuteActionBody = throw new RuntimeException("Implementation is missing!")
}
