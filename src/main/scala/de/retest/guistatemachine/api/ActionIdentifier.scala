package de.retest.guistatemachine.api

import de.retest.surili.commons.actions.Action

class ActionIdentifier(action: Action) extends HashIdentifier(action) {
  val msg = s"ActionIdentifier[action=${action.toString}, hash=$hash]"
  override def toString: String = msg
}
