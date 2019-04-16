package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState

class SutStateIdentifier(hash: String, val sutStateMsg: String = "Unknown") extends HashIdentifier(hash) {
  val msg = s"SutStateIdentifier[sutState=$sutStateMsg, hash=$hash]"

  def this(sutState: SutState) {
    this(HashIdentifier.sha256Hash(sutState), sutState.toString)
  }
  override def toString: String = msg
}
