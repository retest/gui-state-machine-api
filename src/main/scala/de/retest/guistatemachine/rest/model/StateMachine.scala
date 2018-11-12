package de.retest.guistatemachine.rest.model

/**
 * State machine which represents a GUI test.
 */
final case class StateMachine(states: States = States(Map.fromValues[State](State(Transitions()))), actions: Actions = Actions(Map()))