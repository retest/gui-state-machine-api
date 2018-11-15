package de.retest.guistatemachine.rest.json

import de.retest.guistatemachine.api.Id

import spray.json.DefaultJsonProtocol._

trait DefaultJsonFormats {
  // formats for unmarshalling and marshalling
  implicit val idFormat = jsonFormat1(Id)
  implicit val getStateBodyFormat = new JsonFormatForGetStateBody
  implicit val executeActionBodyFormat = new JsonFormatForExecuteActionBody
  implicit val stateFormat = new JsonFormatForState
  implicit val guiStateMachineFormat = new JsonFormatForGuiStateMachine
}
