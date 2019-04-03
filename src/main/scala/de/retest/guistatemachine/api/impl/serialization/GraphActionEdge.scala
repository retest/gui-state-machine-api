package de.retest.guistatemachine.api.impl.serialization
import de.retest.guistatemachine.api.{ActionIdentifier, SutStateIdentifier}

case class GraphActionEdge(from: SutStateIdentifier, to: SutStateIdentifier, action: ActionIdentifier) {
  override def toString: String = action.toString
}
