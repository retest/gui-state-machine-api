package de.retest.guistatemachine.api

import de.retest.surili.commons.actions.Action

class ActionIdentifier(hash: String) extends HashIdentifier(hash) {
  var msg = s"ActionIdentifier[action=Unknown, hash=$hash]"

  def this(action: Action) = {
    this(HashIdentifier.sha256Hash(action))
    msg = s"ActionIdentifier[action=${action.toString}, hash=$hash]"
  }
  override def toString: String = msg
}
