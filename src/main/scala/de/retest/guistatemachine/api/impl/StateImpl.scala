package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, Descriptors, State}

import scala.collection.immutable.{HashMap, HashSet}

class StateImpl(val descriptors: Descriptors, var neverExploredActions: Set[Action]) extends State {

  /**
    * TODO #4 Currently, there is no MultiMap trait for immutable maps in the Scala standard library.
    * The legacy code used `AmbigueState` here which was more complicated than just a multi map.
    */
  var transitions = new HashMap[Action, Set[State]]

  override def getDescriptors: Descriptors = descriptors
  override def getNeverExploredActions: Set[Action] = neverExploredActions
  override def getTransitions: Map[Action, Set[State]] = transitions

  private[api] override def addTransition(a: Action, to: State): Unit = {
    if (!transitions.contains(a)) {
      transitions = transitions + (a -> HashSet(to))
      // In the legacy code this is done in `increaseTimesExecuted`.
      neverExploredActions = neverExploredActions - a
    } else {
      transitions = transitions + (a -> (transitions(a) + to))
    }
  }
}