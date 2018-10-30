package de.retest.guistatemachine.dsl

// TODO Make constructor private pls
case class Transition(from: State, a: Action, var to: State = null) {

  def -(to: State): State = {
    to.previous = from
    this.to = to
    to
  }
}