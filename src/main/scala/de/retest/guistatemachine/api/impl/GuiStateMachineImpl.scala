package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, Descriptors, GuiStateMachine, State}
import scala.collection.immutable.{HashMap, HashSet}
class GuiStateMachineImpl extends GuiStateMachine {
  var states = new HashMap[Descriptors, State]

  /**
    * In the legacy code we had `getAllNeverExploredActions` which had to collect them from all states and make sure they were never executed.
    * Storing them directly in a set improves efficiency.
    */
  var allNeverExploredActions = new HashSet[Action]

  /**
    * The legacy code stored execution counters for every action.
    */
  var allExploredActions = new HashSet[Action]

  /**
    * `actionExecutionCounter` from the legacy code.
    * Stores the total number of executions per action.
    */
  var actionExecutionTimes = new HashMap[Action, Int]

  override def getState(descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    if (states.contains(descriptors)) {
      states(descriptors)
    } else {
      allNeverExploredActions = allNeverExploredActions ++ (neverExploredActions -- allExploredActions)
      val s = new StateImpl(descriptors, neverExploredActions)
      states = states + (descriptors -> s)
      s
    }
  }

  override def executeAction(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    val to = getState(descriptors, neverExploredActions)
    allExploredActions = allExploredActions + a
    allNeverExploredActions = allNeverExploredActions - a
    val old = actionExecutionTimes.get(a)
    old match {
      case Some(o) => actionExecutionTimes = actionExecutionTimes + (a -> (o + 1))
      case None    => actionExecutionTimes = actionExecutionTimes + (a -> 1)
    }
    from.addTransition(a, to)
    to
  }

  override def getAllNeverExploredActions: Set[Action] = allNeverExploredActions

  override def getAllExploredActions: Set[Action] = allExploredActions

  override def getActionExecutionTimes: Map[Action, Int] = actionExecutionTimes
}
