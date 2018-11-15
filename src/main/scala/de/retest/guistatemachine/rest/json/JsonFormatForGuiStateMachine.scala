package de.retest.guistatemachine.rest.json

import de.retest.guistatemachine.api.GuiStateMachine
import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import spray.json.{JsObject, JsValue, RootJsonFormat}

class JsonFormatForGuiStateMachine extends RootJsonFormat[GuiStateMachine] {

  // TODO #1 Convert a GuiStateMachine into JSON
  override def write(obj: GuiStateMachine): JsValue = JsObject()

  // TODO #1 Convert JSON into GuiStateMachine
  override def read(json: JsValue): GuiStateMachine = new GuiStateMachineImpl()
}
