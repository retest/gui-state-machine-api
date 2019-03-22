package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{ActionTransitions, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action

import scala.collection.immutable.HashMap

@SerialVersionUID(1L)
case class StateImpl(sutState: SutState) extends State with Serializable {

  /**
    * Currently, there is no MultiMap trait for immutable maps in the Scala standard library.
    * The legacy code used `AmbigueState` here which was more complicated than just a multi map.
    */
  var transitions = HashMap[Action, ActionTransitions]()

  override def getSutState: SutState = this.synchronized { sutState }
  override def getTransitions: Map[Action, ActionTransitions] = this.synchronized { transitions }

  private[api] override def addTransition(a: Action, to: State): Int = this.synchronized {
    val old = transitions.get(a)
    old match {
      case Some(o) =>
        val updated = ActionTransitions(o.to + to, o.executionCounter + 1)
        transitions = transitions + (a -> updated)
        updated.executionCounter

      case None =>
        transitions += (a -> ActionTransitions(Set(to), 1))
        1
    }
  }
}
