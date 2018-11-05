package de.retest.guistatemachine.api

/**
  * A state should be identified by its corresponding [[Descriptors]].
  * It consists of actions which have not been explored yet and transitions which build up the state machine.
  */
trait State {
  def getNeverExploredActions: Set[Action]

  /**
    * NFA states can lead to different states by consuming the same symbol.
    * Hence, we have a set of states per action.
    */
  def getTransitions: Map[Action, Set[State]]
}
