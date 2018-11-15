package de.retest.guistatemachine.rest

import de.retest.guistatemachine.rest.model.{Action, Actions, Id, State, StateMachine, StateMachines, States, Transition, Transitions}
import spray.json.DefaultJsonProtocol._

trait DefaultJsonFormats {
  // formats for unmarshalling and marshalling
  implicit val idFormat = jsonFormat1(Id)
  implicit val actionFormat = jsonFormat0(Action)
  implicit val idMapFormatActions = new JsonFormatForIdMap[Action]
  implicit val actionsFormat = jsonFormat1(Actions)
  implicit val transitionFormat = jsonFormat2(Transition)
  implicit val idMapFormatTransitions = new JsonFormatForIdMap[Transition]
  implicit val transitionsFormat = jsonFormat1(Transitions)
  implicit val stateFormat = jsonFormat1(State)
  implicit val idMapFormatState = new JsonFormatForIdMap[State]
  implicit val statesFormat = jsonFormat1(States)
  implicit val stateMachineFormat = jsonFormat2(StateMachine)
  implicit val idMapFormatStateMachines = new JsonFormatForIdMap[StateMachine]
  implicit val stateMachinesFormat = jsonFormat1(StateMachines)
}
