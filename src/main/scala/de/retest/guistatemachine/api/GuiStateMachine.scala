package de.retest.guistatemachine.api

/**
  * API to create a NFA which represents the current state machine of an automatic GUI test generation with the help of a genetic algorithm.
  * Simulated actions by the user are mapped to transitions in the state machine.
  * States are identified by descriptors.
  * There can be ambiguous states which makes the finite state machine non-deterministic.
  * There can also be multiple start states for NFAs.
  * Therefore, we do not provide any functionality to set or get the initial state.
  */
trait GuiStateMachine {

  /**
    * Gets a state identified by descriptors and with its initial never explored actions.
    *
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

  /**
    * Can be used by the GA to generate new test cases.
    *
    * @return All actions which have not been explored yet.
    */
  def getAllNeverExploredActions: Set[Action]

  /**
    * In the legacy code this was only used to show the number of actions which have been explored by Monkey Testing.
    *
    * @return All actions which have been explored and therefore have a corresponding transition.
    */
  def getAllExploredActions: Set[Action]

  /**
    * In the legacy code this was only used to calculate [[getAllNeverExploredActions]].
    * It could be used for the visualization of the NFA to see how often actions are executed.
    *
    * @return The number of times every explored action has been executed in the NFA.
    */
  def getActionExecutionTimes: Map[Action, Int]
}
