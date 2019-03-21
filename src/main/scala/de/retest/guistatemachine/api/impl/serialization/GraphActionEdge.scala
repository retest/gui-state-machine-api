package de.retest.guistatemachine.api.impl.serialization
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action

case class GraphActionEdge(from: SutState, to: SutState, action: Action) {
  override def toString: String = action.toString
}
