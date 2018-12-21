package de.retest.guistatemachine.api.impl
import de.retest.surili.model.Action
import de.retest.ui.descriptors.SutState

case class GraphActionEdge(from: SutState, to: SutState, action: Action) {
  override def toString: String = action.toString
}
