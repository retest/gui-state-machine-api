package de.retest.guistatemachine.api

import de.retest.ui.descriptors.SutState

/**
  * Set of root elements which identifies a state.
  */
@SerialVersionUID(1L)
case class Descriptors(sutState: SutState) extends Serializable
