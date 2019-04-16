package de.retest.guistatemachine.api

/**
  * Represents transitions for one single symbol which is represented by an `de.retest.surili.commons.actions.Action` to a number of states.
  * The corresponding symbol is not stored in this class but in the [[State]] from which the transitions are started or which the transitions lead to.
  *
  * @param states The states which the transitions lead to or start from. Since it is a NFA, there can be multiple states for the same symbol.
  */
@SerialVersionUID(1L)
case class ActionTransitions(states: Set[State]) extends Serializable
