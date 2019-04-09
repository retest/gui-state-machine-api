package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState

class SutStateIdentifier(hash: String) extends HashIdentifier(hash) {
  var msg = s"SutStateIdentifier[sutState=Unknown, hash=$hash]"

  def this(sutState: SutState) {
    this(HashIdentifier.sha256Hash(sutState))
    msg = s"SutStateIdentifier[sutState=${sutState.toString}, hash=$hash]"
  }
  override def toString: String = msg
}
