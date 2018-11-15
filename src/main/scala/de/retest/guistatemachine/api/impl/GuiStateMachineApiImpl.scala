package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi, Id}

import scala.collection.immutable.HashMap

object GuiStateMachineApiImpl extends GuiStateMachineApi {
  val stateMachines = IdMap(new HashMap[Id, GuiStateMachine])

  override def createStateMachine(): Id = stateMachines.addNewElement(new GuiStateMachineImpl)

  override def removeStateMachine(id: Id): Boolean = stateMachines.removeElement(id)

  override def getStateMachine(id: Id): Option[GuiStateMachine] = stateMachines.getElement(id)

  override def persist(): Unit = {
    // TODO #9 store on the disk
  }

  override def load(): Unit = {
    // TODO #9 Load from the disk
  }
}
