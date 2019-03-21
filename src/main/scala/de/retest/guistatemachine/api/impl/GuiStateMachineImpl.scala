package de.retest.guistatemachine.api.impl

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action

import scala.collection.immutable.{HashMap, HashSet}

/**
  * Thread-safe implementation of a GUI state machine.
  */
@SerialVersionUID(1L)
class GuiStateMachineImpl extends GuiStateMachine with Serializable {
  @transient private val logger = Logger[GuiStateMachineImpl]
  private var states = new HashMap[SutState, State]

  /**
    * The legacy code stored execution counters for every action.
    */
  private var allExploredActions = new HashSet[Action]

  /**
    * `actionExecutionCounter` from the legacy code.
    * Stores the total number of executions per action.
    */
  private var actionExecutionTimes = new HashMap[Action, Int]

  override def getState(sutState: SutState): State = this.synchronized {
    if (states.contains(sutState)) {
      states(sutState)
    } else {
      logger.info(s"Create new state from SUT state with hash code ${sutState.hashCode()}")
      val s = StateImpl(sutState)
      states += (sutState -> s)
      s
    }
  }

  override def executeAction(from: State, a: Action, to: State): State = this.synchronized {
    allExploredActions += a
    val old = actionExecutionTimes.get(a)
    old match {
      case Some(o) => actionExecutionTimes += (a -> (o + 1))
      case None    => actionExecutionTimes += (a -> 1)
    }
    from.addTransition(a, to)
    to
  }

  override def getAllStates: Map[SutState, State] = this.synchronized { states }

  override def getAllExploredActions: Set[Action] = this.synchronized { allExploredActions }

  override def getActionExecutionTimes: Map[Action, Int] = this.synchronized { actionExecutionTimes }

  override def clear(): Unit = this.synchronized {
    states = new HashMap[SutState, State]
    allExploredActions = new HashSet[Action]
    actionExecutionTimes = new HashMap[Action, Int]
  }

  override def assignFrom(other: GuiStateMachine): Unit = this.synchronized {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineImpl]
    states = otherStateMachine.states
    allExploredActions = otherStateMachine.allExploredActions
    actionExecutionTimes = otherStateMachine.actionExecutionTimes
  }
}
