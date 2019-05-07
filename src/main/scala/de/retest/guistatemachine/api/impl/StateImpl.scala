package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{ActionIdentifier, ActionTransitions, State, SutStateIdentifier}

import scala.collection.immutable.{HashMap, HashSet}

@SerialVersionUID(1L)
case class StateImpl(sutState: SutStateIdentifier) extends State with Serializable {

  /**
    * Currently, there is no MultiMap trait for immutable maps in the Scala standard library.
    * The legacy code used `AmbigueState` here which was more complicated than just a multi map.
    */
  private var outgoingActionTransitions = HashMap[ActionIdentifier, ActionTransitions]()

  /**
    * Redundant information but helpful to be retrieved.
    */
  private var incomingActionTransitions = HashMap[ActionIdentifier, ActionTransitions]()

  private var possibleActions: Set[ActionIdentifier] = HashSet[ActionIdentifier]()

  override def getSutStateIdentifier: SutStateIdentifier = this.synchronized { sutState }
  override def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions] = this.synchronized {
    outgoingActionTransitions
  }
  override def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions] = this.synchronized {
    incomingActionTransitions
  }
  override def setPossibleActions(possibleActions: Set[ActionIdentifier]): Unit = this.synchronized {
    this.possibleActions = possibleActions
  }
  override def getPossibleActions: Set[ActionIdentifier] = this.synchronized { possibleActions }

  private[api] override def addTransition(a: ActionIdentifier, to: State): Int = {
    val executionCounter = this.synchronized {
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
