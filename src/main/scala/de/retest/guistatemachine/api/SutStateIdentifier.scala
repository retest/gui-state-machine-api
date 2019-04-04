package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState

class SutStateIdentifier(sutState: SutState) extends HashIdentifier(sutState) {
  val msg = s"SutStateIdentifier[sutState=${sutState.toString}, hash=$hash]"
  override def toString: String = msg
}
