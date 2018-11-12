package de.retest.guistatemachine.api

/**
  * A state should be identified by its corresponding [[Descriptors]].
  * It consists of actions which have not been explored yet and transitions which build up the state machine.
  */
trait State {
  def getDescriptors: Descriptors

  def getNeverExploredActions: Set[Action]

  /**
    * NFA states can lead to different states by consuming the same symbol.
    * Hence, we have a set of states per action.
    */
  def getTransitions: Map[Action, Set[State]]

  /**
    * Adds a new transition to the state which is only allowed by calling the methods of [[GuiStateMachine]].
    * @param a The action which represents the transition's consumed symbol.
    * @param to The state which the transition leads t o.
    */
  private[api] def addTransition(a: Action, to: State): Unit

  /**
    * Overriding this method is required to allow the usage of a set of states.
    * Comparing the descriptors should check for the equality of all root elements which compares the identifying attributes and the contained components
    * for each root element.
    */
  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[State]) {
      val other = obj.asInstanceOf[State]
      this.getDescriptors == other.getDescriptors
    } else {
      super.equals(obj)
    }
  }

  override def hashCode(): Int = this.getDescriptors.hashCode()

  override def toString: String = s"descriptors=${getDescriptors},neverExploredActions=${getNeverExploredActions},transitions=${getTransitions}"
}