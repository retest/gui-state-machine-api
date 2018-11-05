package de.retest.guistatemachine.api

/**
  * API to create a NFA which represents the current state machine of an automatic GUI test generation with the help of a genetic algorithm.
  * Simulated actions by the user are mapped to transitions in the state machine.
  * States are identified by descriptors.
  * There can be ambigious states which makes the finite state machine non-deterministic.
  */
trait GuiStateMachine {

  /**
    * Gets a state identified by descriptors and with its initial never explored actions.
    * @param descriptors The descriptors which identify the state.
    * @param neverExploredActions All actions which have never been explored from the state.
    * @return The state identified by the descriptors. If there has not been any state yet, it will be added.
    */
  def getState(descriptors: Descriptors, neverExploredActions: Set[Action]): State

  /**
    * Executes an action from a state leading to the current state described by descriptors.
    *
    * @param from The state the action is executed from
    * @param a The action which is executed by the user.
    * @param descriptors The descriptors which identify the state which the action leads to and which is returned by this method.
    * @param neverExploredActions The never explored actions of the state which the action leads to and which is returned by this method.
    * @return The current state which the transition of a leads to.
    */
  def executeAction(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action]): State
}
