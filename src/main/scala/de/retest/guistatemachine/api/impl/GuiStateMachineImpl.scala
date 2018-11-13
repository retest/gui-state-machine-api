package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, Descriptors, GuiStateMachine, State}

import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class GuiStateMachineImpl extends GuiStateMachine {
  val states = new HashMap[Descriptors, State]

  /**
    * In the legacy code we had `getAllNeverExploredActions` which had to collect them from all states and make sure they were never executed.
    * Storing them directly in a set improves efficiency.
    */
  val allNeverExploredActions = new HashSet[Action]

  /**
    * The legacy code stored execution counters for every action.
    */
  val allExploredActions = new HashSet[Action]

  override def getState(descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    if (states.contains(descriptors)) {
      states(descriptors)
    } else {
      allNeverExploredActions ++= (neverExploredActions -- allExploredActions)
      val s = new StateImpl(descriptors, neverExploredActions)
      states += (descriptors -> s)
      s
    }
  }

  override def executeAction(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    val to = getState(descriptors, neverExploredActions)
    allExploredActions += a
    allNeverExploredActions -= a
    from.addTransition(a, to)
    to
  }

  override def getAllNeverExploredActions: scala.collection.mutable.Set[Action] = allNeverExploredActions

  override def getAllExploredActions: scala.collection.mutable.Set[Action] = allExploredActions
}