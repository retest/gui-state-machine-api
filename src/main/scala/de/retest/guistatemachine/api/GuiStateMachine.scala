package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.{Action, ActionType}

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
    * Creates a new state in the state machine based on a SUT state.
    *
    * @param sutStateIdentifier The abstract representation of a SUT state which should uniquely identify the state.
    * @param unexploredActionTypes The initially unexplored action types. This set is modified whenever an action is executed which has an unexplored action type.
    * @return The newly created state.
    */
  def createState(sutStateIdentifier: SutStateIdentifier, unexploredActionTypes: Set[ActionType]): State

  def createState(sutState: SutState, unexploredActionTypes: Set[ActionType]): State =
    createState(new SutStateIdentifier(sutState), unexploredActionTypes)

  def getState(sutStateIdentifier: SutStateIdentifier): Option[State]

  /**
    * Gets a state identified by the corresponding SUT state.
    *
    * @param sutState The SUT state which identifies the state.
    * @return The state identified by the descriptors. If there has not been any state yet, it will be added.
    */
  def getState(sutState: SutState): Option[State] = getState(new SutStateIdentifier(sutState))

  /**
    * Executes an action from a state leading to the current state described by descriptors.
    *
    * @param from The state the action is executed from
    * @param a The action which is executed by the user.
    * @param to The state which the execution leads to.
    * @param actionType The corresponding action type of a.
    * @return The number of times the action has been executed.
    */
  def executeAction(from: State, a: ActionIdentifier, to: State, actionType: ActionType): Int = from.addTransition(a, to, actionType)
  def executeAction(from: State, a: Action, to: State): Int =
    executeAction(from, new ActionIdentifier(a), to, ActionType.fromAction(a))

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
