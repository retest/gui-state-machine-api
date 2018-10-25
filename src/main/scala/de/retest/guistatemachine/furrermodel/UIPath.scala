package de.retest.guistatemachine.furrermodel

import scala.annotation.tailrec

class PathTransition(val action: UIAction, val next: PathState) {
}

class PathState(val state: State, val transition: PathTransition = null) {
}

/**
 * The path of a test case which starts with the initial state of the GUI application.
 */
class UIPath(initial: PathState) {

  /**
   * @return The final state which is reached by the path.
   */
  def getFinalState: PathState = {
    @tailrec
    def getNextPathState(current: PathState): PathState = if (current.transition eq null) current else getNextPathState(current.transition.next)

    getNextPathState(initial)
  }
}