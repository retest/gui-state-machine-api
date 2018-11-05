package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, Descriptors, State}

import scala.collection.immutable.{HashMap, HashSet}

class StateImpl(val descriptors: Descriptors, var neverExploredActions: Set[Action]) extends State {

  /**
    * TODO #4 Currently, there is no MultiMap trait for immutable maps.
    */
  var transitions = new HashMap[Action, Set[State]]

  override def getNeverExploredActions: Set[Action] = neverExploredActions
  override def getTransitions: Map[Action, Set[State]] = transitions

  def addTransition(a: Action, to: State): Unit = {
    if (!transitions.contains(a)) {
      transitions = transitions + (a -> HashSet(to))
      // TODO #4 This is not done in the legacy code:
      neverExploredActions = neverExploredActions - a
    } else {
      transitions = transitions + (a -> (transitions(a) + to))
    }
  }

  /**
    * Overriding this method is required to allow the usage of a set of states.
    * Comparing the descriptors should check for the equality of all root elements which compares the identifying attributes and the contained components
    * for each root element.
    */
  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[StateImpl]) {
      val other = obj.asInstanceOf[StateImpl]
      this.descriptors == other.descriptors
    } else {
      super.equals(obj)
    }
  }

  override def hashCode(): Int = this.descriptors.hashCode()
}
