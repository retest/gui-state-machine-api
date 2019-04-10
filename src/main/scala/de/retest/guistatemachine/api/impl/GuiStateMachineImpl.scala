package de.retest.guistatemachine.api.impl

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State, SutStateIdentifier}

import scala.collection.concurrent.TrieMap

/**
  * Thread-safe implementation of a GUI state machine.
  */
@SerialVersionUID(1L)
class GuiStateMachineImpl extends GuiStateMachine with Serializable {
  @transient private val logger = Logger[GuiStateMachineImpl]
  private var states = TrieMap[SutStateIdentifier, State]()

  override def getState(sutState: SutStateIdentifier): State =
    if (states.contains(sutState)) {
      states(sutState)
    } else {
      logger.info(s"Create new state from SUT state $sutState")
      val s = StateImpl(sutState)
      states += (sutState -> s)
      s
    }

  override def getAllStates: Map[SutStateIdentifier, State] = states.toMap

  override def clear(): Unit = {
    states = TrieMap[SutStateIdentifier, State]()
  }

  override def assignFrom(other: GuiStateMachine): Unit = {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineImpl]
    states = otherStateMachine.states
  }
}
