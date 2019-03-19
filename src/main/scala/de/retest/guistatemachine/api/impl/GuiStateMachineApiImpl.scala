package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi, Id}

/**
  * Thread-safe implementation of the API. It is thread-safe because it uses `IdMap`.
  */
class GuiStateMachineApiImpl extends GuiStateMachineApi {
  private val stateMachines = IdMap[GuiStateMachine]()

  override def createStateMachine(): Id = stateMachines.addNewElement(new GuiStateMachineImpl)

  override def removeStateMachine(id: Id): Boolean = stateMachines.removeElement(id)

  override def getStateMachine(id: Id): Option[GuiStateMachine] = stateMachines.getElement(id)

  override def clear(): Unit = stateMachines.clear()
}
