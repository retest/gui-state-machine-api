package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action

/**
  * API to create a NFA which represents the current state machine of an automatic GUI test generation with the help of a genetic algorithm.
  * Simulated actions by the user are mapped to transitions in the state machine.
  * States are identified by `SutState` instances.
  * There can be ambiguous states which makes the finite state machine non-deterministic.
  * There can also be multiple start states for NFAs.
  * Therefore, we do not provide any functionality to set or get the initial state.
  */
trait GuiStateMachine {

  /**
    * Gets a state identified by the corresponding SUT state.
    *
    * @param sutState The SUT state which identifies the state.
    * @return The state identified by the descriptors. If there has not been any state yet, it will be added.
    */
  def getState(sutState: SutState): State

  /**
    * Executes an action from a state leading to the current state described by descriptors.
    *
    * @param from The state the action is executed from
    * @param a The action which is executed by the user.
    * @param to The state which the execution leads to.
    * @return The current state which the transition of a leads to.
    */
  def executeAction(from: State, a: Action, to: State): State
  def executeAction(fromSutState: SutState, a: Action, toSutState: SutState): State = executeAction(getState(fromSutState), a, getState(toSutState))

  def getAllStates: Map[SutState, State]

  /**
    * In the legacy code this was only used to show the number of actions which have been explored by Monkey Testing.
    *
    * @return All actions which have been explored and therefore have a corresponding transition.
    */
  def getAllExploredActions: Set[Action]

  /**
    * In the legacy code this was only used to calculate all never explored actions.
    * It could be used for the visualization of the NFA to see how often actions are executed.
    *
    * @return The number of times every explored action has been executed in the NFA. Never explored actions are not part of it.
    */
  def getActionExecutionTimes: Map[Action, Int]

  /**
    * Clears all states, transitions and never explored actions etc.
    */
  def clear(): Unit

  /**
    * Stores the state machine on the disk.
    * Persistence can be useful when the state machines become quite big and the generation/modification is interrupted
    * and continued later.
    *
    * @param filePath The file which the state machine is stored into.
    */
  def save(filePath: String): Unit

  /**
    * Clears the state machine and loads it from the disk.
    *
    * @param filePath The file which the state machine is loaded from.
    */
  def load(filePath: String): Unit

  /**
    * Converts the state machines into GML which can be read by editors like yED.
    *
    * @param filePath The file which the GML data is stored into.
    * @throws RuntimeException If a vertex or edge cannot be added, this exception is thrown.
    */
  def saveGML(filePath: String): Unit
}
