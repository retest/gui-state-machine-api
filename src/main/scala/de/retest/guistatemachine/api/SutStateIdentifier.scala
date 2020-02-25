package de.retest.guistatemachine.api

import de.retest.recheck.ui.descriptors.SutState

class SutStateIdentifier(sutState: SutState) extends HashIdentifier(sutState) {
  val msg = s"[sutState=${sutState.toString}, hash=${hash.substring(0, 8)}]"
  override def toString: String = msg
}
