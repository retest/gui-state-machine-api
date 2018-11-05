package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{Action, Descriptors, GuiStateMachine, State}

import scala.collection.mutable.HashMap

object GuiStateMachineImpl extends GuiStateMachine {
  val states = new HashMap[Descriptors, State]

  override def getState(descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    if (states.contains(descriptors)) {
      states(descriptors)
    } else {
      val s = new StateImpl(descriptors, neverExploredActions)
      states += (descriptors -> s)
      s
    }
  }

  override def executeAction(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action]): State = {
    val to = getState(descriptors, neverExploredActions)
    from.asInstanceOf[StateImpl].addTransition(a, to)
    to
  }
}
