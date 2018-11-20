package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, ActionTransitions, Descriptors, State}

import scala.collection.immutable.HashMap

@SerialVersionUID(1L)
class StateImpl(descriptors: Descriptors, var neverExploredActions: Set[Action]) extends State with Serializable {

  /**
    * TODO #4 Currently, there is no MultiMap trait for immutable maps in the Scala standard library.
    * The legacy code used `AmbigueState` here which was more complicated than just a multi map.
    */
  var transitions = new HashMap[Action, ActionTransitions]

  override def getDescriptors: Descriptors = descriptors
  override def getNeverExploredActions: Set[Action] = neverExploredActions
  override def getTransitions: Map[Action, ActionTransitions] = transitions

  private[api] override def addTransition(a: Action, to: State): Int = {
    val old = transitions.get(a)
    old match {
      case Some(o) => {
        val updated = ActionTransitions(o.to + to, o.executionCounter + 1)
        transitions = transitions + (a -> updated)
        updated.executionCounter
      }

      case None => {
        transitions += (a -> ActionTransitions(Set(to), 1))
        // In the legacy code this is done in `increaseTimesExecuted`.
        neverExploredActions = neverExploredActions - a
        1
      }
    }
  }
}
