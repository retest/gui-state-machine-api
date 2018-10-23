package de.retest.guistatemachine

class PathState(state : State, transition : UIAction, next : PathState) {
}

trait UIPath {
  private val states = Seq[PathState]()
}