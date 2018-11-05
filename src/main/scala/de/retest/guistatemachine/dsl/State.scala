package de.retest.guistatemachine.dsl

import scala.collection.mutable.ListBuffer

trait State {
  /**
   * The previous state has to be stored for the DSL only to reach the initial state.
   */
  private[dsl] var previous: State = null
  // TODO Use a set instead of a list buffer? Actually it is a multi map with the action as key and multiple possible states as values.
  private[dsl] var transitions: ListBuffer[Transition] = ListBuffer.empty[Transition]

  def getTransitions: Seq[Transition] = transitions

  /**
   * Goes back to the initial state from the current state and returns it.
   */
  def getInitial: InitialState = {
    def getFirst: State = if (previous eq null) this else previous.getInitial

    getFirst.asInstanceOf[InitialState]
  }

  // TODO Rename to ->
  def -(a: Action): Transition = {
    val t = Transition(this, a)
    transitions += t
    t
  }
}