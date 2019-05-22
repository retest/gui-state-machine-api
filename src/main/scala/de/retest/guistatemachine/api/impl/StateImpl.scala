package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{ActionIdentifier, ActionTransitions, State, SutStateIdentifier}
import de.retest.surili.commons.actions.ActionType

import scala.collection.immutable.HashMap

@SerialVersionUID(1L)
case class StateImpl(sutState: SutStateIdentifier, var unexploredActionTypes: Set[ActionType]) extends State with Serializable {

  /**
    * Currently, there is no MultiMap trait for immutable maps in the Scala standard library.
    * The legacy code used `AmbigueState` here which was more complicated than just a multi map.
    */
  private var outgoingActionTransitions = HashMap[ActionIdentifier, ActionTransitions]()

  /**
    * Redundant information but helpful to be retrieved.
    */
  private var incomingActionTransitions = HashMap[ActionIdentifier, ActionTransitions]()

  override def getSutStateIdentifier: SutStateIdentifier = this.synchronized { sutState }
  override def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions] = this.synchronized {
    outgoingActionTransitions
  }
  override def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions] = this.synchronized {
    incomingActionTransitions
  }
  override def getUnexploredActionTypes: Set[ActionType] = this.synchronized { unexploredActionTypes }

  private[api] override def addTransition(a: ActionIdentifier, to: State, actionType: ActionType): Int = {
    val executionCounter = this.synchronized {
      unexploredActionTypes = unexploredActionTypes - actionType

      outgoingActionTransitions.get(a) match {
        case Some(oldTransitions) =>
          val updatedTransitions = ActionTransitions(oldTransitions.states + to, oldTransitions.executionCounter + 1)
          outgoingActionTransitions = outgoingActionTransitions + (a -> updatedTransitions)
          updatedTransitions.executionCounter

        case None =>
          outgoingActionTransitions += (a -> ActionTransitions(Set(to), 1))
          1
      }
    }

    to.synchronized {
      val other = to.asInstanceOf[StateImpl]
      other.incomingActionTransitions.get(a) match {
        case Some(oldTransitions) =>
          val updatedTransitions = ActionTransitions(oldTransitions.states + this, oldTransitions.executionCounter + 1)
          other.incomingActionTransitions = other.incomingActionTransitions + (a -> updatedTransitions)

        case None =>
          other.incomingActionTransitions += (a -> ActionTransitions(Set(this), 1))
      }
    }

    executionCounter
  }
}
