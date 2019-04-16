package de.retest.guistatemachine.api

import de.retest.surili.commons.actions.Action

class ActionIdentifier(hash: String, val actionMsg: String = "Unknown") extends HashIdentifier(hash) {
  val msg = s"ActionIdentifier[action=$actionMsg, hash=$hash]"

  def this(action: Action) = {
    this(HashIdentifier.sha256Hash(action), action.toString)
  }
  override def toString: String = msg
}
