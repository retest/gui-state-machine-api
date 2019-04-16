package de.retest.guistatemachine.api

/**
  * A state should be identified by its corresponding SutState.
  * It consists of actions which have not been explored yet and transitions to states which build up the state machine.
  */
trait State {

  /**
    * @return The SutState which identifies this state.
    */
  def getSutStateIdentifier: SutStateIdentifier

  /**
    * NFA states can lead to different states by consuming the same symbol.
    * Hence, we have a set of states per action.
    * In the legacy code there was a type called `AmbigueState` but a multimap simplifies the implementation.
    */
  def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions]

  /**
    * Helper method to retrieve all incoming transitions.
    */
  def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions]

  /**
    * Overriding this method is required to allow the usage of a set of states.
    * Comparing the descriptors should check for the equality of all root elements which compares the identifying attributes and the contained components
    * for each root element.
    */
  override def equals(obj: Any): Boolean = {
    obj match {
      case other: State =>
        this.getSutStateIdentifier == other.getSutStateIdentifier
      case _ =>
        super.equals(obj)
    }
  }

  override def hashCode(): Int = this.getSutStateIdentifier.hashCode()

  override def toString: String = s"State[sutStateIdentifier=$getSutStateIdentifier]"

  /**
    * Adds a new transition to the state which is only allowed by calling the methods of [[GuiStateMachine]].
    *
    * @param a The action which represents the transition's consumed symbol.
    * @param to The state which the transition leads t o.
    */
  private[api] def addTransition(a: ActionIdentifier, to: State): Unit
}
