package de.retest.guistatemachine.dsl

import scala.collection.mutable.ListBuffer

abstract class State {
  private[dsl] var previous: State = null
  // TODO Use a set?
  private[dsl] var transitions: ListBuffer[Transition] = ListBuffer.empty[Transition]

  def getInitial: InitialState = {
    def getFirst: State = if (previous eq null) this else previous.getInitial

    getFirst.asInstanceOf[InitialState]
  }

  def -(a: Action): Transition = {
    val t = Transition(this, a)
    transitions += t
    t
  }
}