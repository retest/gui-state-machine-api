package de.retest.guistatemachine.api

import scala.util.Random

import de.retest.surili.model.Action

/**
  * A state should be identified by its corresponding [[Descriptors]].
  * It consists of actions which have not been explored yet and transitions to states which build up the state machine.
  */
trait State {

  /**
    * @return The descriptors which identify this state.
    */
  def getDescriptors: Descriptors

  /**
    * @return All actions which have not already been explored/executed from this state.
    */
  def getNeverExploredActions: Set[Action]

  /**
    * NFA states can lead to different states by consuming the same symbol.
    * Hence, we have a set of states per action.
    * In the legacy code there was a type called `AmbigueState` but a multimap simplifies the implementation.
    */
  def getTransitions: Map[Action, ActionTransitions]

  /**
    * This was used in the legacy code for Monkey testing.
    *
    * @return Returns a random action or an empty value if there are none left.
    */
  def getRandomAction(): Option[Action] = {
    val r = getAllActions.toVector
    if (r.isEmpty) { None } else {
      val rnd = new Random()
      Some(r(rnd.nextInt(r.size)))
    }
  }

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

  /**
    * Adds a new transition to the state which is only allowed by calling the methods of [[GuiStateMachine]].
    *
    * @param a The action which represents the transition's consumed symbol.
    * @param to The state which the transition leads t o.
    * @return The number of times the action has been executed from this state. The target state does not matter for this number.
    */
  private[api] def addTransition(a: Action, to: State): Int

  /**
    * This was named `getRandomActions` in the legacy code but actually returned all actions.
    *
    * @return All actions (explored + unexplored).
    */
  private def getAllActions(): Set[Action] = getNeverExploredActions ++ getTransitions.keySet
}
