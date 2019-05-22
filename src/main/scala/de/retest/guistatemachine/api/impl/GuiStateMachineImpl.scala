package de.retest.guistatemachine.api.impl

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State, SutStateIdentifier}
import de.retest.surili.commons.actions.ActionType

import scala.collection.mutable.HashMap

/**
  * Thread-safe implementation of a GUI state machine.
  */
@SerialVersionUID(1L)
class GuiStateMachineImpl extends GuiStateMachine with Serializable {
  @transient private val logger = Logger[GuiStateMachineImpl]
  private var states = HashMap[SutStateIdentifier, State]()

  override def createState(sutStateIdentifier: SutStateIdentifier, unexploredActionTypes: Set[ActionType]): State =
    this.synchronized {
      states.get(sutStateIdentifier) match {
        case Some(s) =>
          logger.warn(s"State from SUT state $sutStateIdentifier does already exist.")
          s
        case None =>
          logger.info(s"Create new state from SUT state $sutStateIdentifier")
          val s = StateImpl(sutStateIdentifier, unexploredActionTypes)
          states += (sutStateIdentifier -> s)
          s
      }
    }

  override def getState(sutStateIdentifier: SutStateIdentifier): Option[State] = this.synchronized {
    states.get(sutStateIdentifier)
  }

  override def getAllStates: Map[SutStateIdentifier, State] = states.toMap

  override def clear(): Unit = this.synchronized {
    states.clear()
  }

  override def assignFrom(other: GuiStateMachine): Unit = this.synchronized {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineImpl]
    states = otherStateMachine.states
  }
}
