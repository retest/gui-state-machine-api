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

  def getState(sutStateIdentifier: SutStateIdentifier): State

  /**
    * Gets a state identified by the corresponding SUT state.
    *
    * @param sutState The SUT state which identifies the state.
    * @return The state identified by the descriptors. If there has not been any state yet, it will be added.
    */
  def getState(sutState: SutState): State = getState(new SutStateIdentifier(sutState))

  /**
    * Executes an action from a state leading to the current state described by descriptors.
    *
    * @param from The state the action is executed from
    * @param a The action which is executed by the user.
    * @param to The state which the execution leads to.
    * @return The number of times the action has been executed.
    */
  def executeAction(from: State, a: ActionIdentifier, to: State): Int = from.addTransition(a, to)
  def executeAction(from: State, a: Action, to: State): Int = executeAction(from, new ActionIdentifier(a), to)
  def executeAction(fromSutState: SutState, a: Action, toSutState: SutState): Int =
    executeAction(getState(fromSutState), a, getState(toSutState))

  def getAllStates: Map[SutStateIdentifier, State]

  /**
    * Clears all states, transitions and never explored actions etc.
    */
  def clear(): Unit

  /**
    * Clears the current states and assigns them from another state machine.
    *
    * @param other The other state machine.
    */
  def assignFrom(other: GuiStateMachine): Unit
}
