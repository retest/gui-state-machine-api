package de.retest.guistatemachine.dsl

import scala.collection.immutable.HashSet

/**
 * NFA:
 * 5–Tupel (Z, Σ, δ, z0, E)
 * TODO NFAs can have multiple initial states. Do we really need this?
 * TODO Make the constructor private.
 */
final case class StateMachine(initial: InitialState, var previous: StateMachine) {
  /**
   * Appends another state machine.
   */
  def ~(s: StateMachine): StateMachine = {
    s.previous = this
    s
  }

  /**
   * All states.
   */
  //def Z(): Set[State]
  /**
   * Input alphabet.
   */
  //def Σ(): Set[Action]
  /**
   * Partially defined function which returns the next state.
   */
  //def δ(s: State, a: Action): State
  /**
   * Initial state.
   * TODO NFAs can have multiple initial states. Do we really need this?
   */
  //def z0: InitialState = initial

  /**
   * All final states.
   */
  //def E: Set[FinalState]
}

object StateMachine {
  // TODO f should return a FinalState since all state machines have to end with one
  def apply(f: => State): StateMachine = StateMachine(f.getInitial, null)
}