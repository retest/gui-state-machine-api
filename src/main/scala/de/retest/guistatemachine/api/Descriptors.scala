package de.retest.guistatemachine.api

import de.retest.ui.descriptors.RootElement

/**
  * Set of root elements which identifies a state.
  */
@SerialVersionUID(1L)
case class Descriptors(rootElements: Set[RootElement]) extends Serializable
