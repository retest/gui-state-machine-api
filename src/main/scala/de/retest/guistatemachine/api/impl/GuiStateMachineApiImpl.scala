package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}

import scala.collection.concurrent.TrieMap

/**
  * Thread-safe implementation of the API.
  */
class GuiStateMachineApiImpl extends GuiStateMachineApi {
  private val stateMachines = TrieMap[String, GuiStateMachine]()

  override def createStateMachine(name: String): GuiStateMachine = {
    val guiStateMachine = new GuiStateMachineImpl
    stateMachines += (name -> guiStateMachine)
    guiStateMachine
  }

  override def removeStateMachine(name: String): Boolean = stateMachines.remove(name).isDefined

  override def getStateMachine(name: String): Option[GuiStateMachine] = stateMachines.get(name)

  override def clear(): Unit = stateMachines.clear()
}
