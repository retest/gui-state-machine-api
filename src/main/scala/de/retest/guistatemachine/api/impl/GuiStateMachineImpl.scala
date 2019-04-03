package de.retest.guistatemachine.api.impl

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{ActionIdentifier, GuiStateMachine, State, SutStateIdentifier}

import scala.collection.immutable.{HashMap, HashSet}

/**
  * Thread-safe implementation of a GUI state machine.
  */
@SerialVersionUID(1L)
class GuiStateMachineImpl extends GuiStateMachine with Serializable {
  @transient private val logger = Logger[GuiStateMachineImpl]
  private var states = new HashMap[SutStateIdentifier, State]

  /**
    * The legacy code stored execution counters for every action.
    */
  private var allExploredActions = new HashSet[ActionIdentifier]

  /**
    * `actionExecutionCounter` from the legacy code.
    * Stores the total number of executions per action.
    */
  private var actionExecutionTimes = new HashMap[ActionIdentifier, Int]

  override def getState(sutState: SutStateIdentifier): State = this.synchronized {
    if (states.contains(sutState)) {
      states(sutState)
    } else {
      logger.info(s"Create new state from SUT state $sutState")
      val s = StateImpl(sutState)
      states += (sutState -> s)
      s
    }
  }

  override def executeAction(from: State, a: ActionIdentifier, to: State): State = this.synchronized {
    allExploredActions += a
    val old = actionExecutionTimes.get(a)
    old match {
      case Some(o) => actionExecutionTimes += (a -> (o + 1))
      case None    => actionExecutionTimes += (a -> 1)
    }
    from.addTransition(a, to)
    to
  }

  override def getAllStates: Map[SutStateIdentifier, State] = this.synchronized { states }

  override def getAllExploredActions: Set[ActionIdentifier] = this.synchronized { allExploredActions }

  override def getActionExecutionTimes: Map[ActionIdentifier, Int] = this.synchronized { actionExecutionTimes }

  override def clear(): Unit = this.synchronized {
    states = new HashMap[SutStateIdentifier, State]
    allExploredActions = new HashSet[ActionIdentifier]
    actionExecutionTimes = new HashMap[ActionIdentifier, Int]
  }

  override def assignFrom(other: GuiStateMachine): Unit = this.synchronized {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineImpl]
    states = otherStateMachine.states
    allExploredActions = otherStateMachine.allExploredActions
    actionExecutionTimes = otherStateMachine.actionExecutionTimes
  }
}
