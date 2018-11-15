package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.GuiStateMachineApi
import de.retest.guistatemachine.api.GuiStateMachine

import scala.collection.mutable.HashSet

object GuiStateMachineApiImpl extends GuiStateMachineApi {
  val stateMachines = new HashSet[GuiStateMachine]

  override def createStateMachine: GuiStateMachine = {
    val r = new GuiStateMachineImpl
    stateMachines += r
    r
  }

  override def removeStateMachine(stateMachine: GuiStateMachine): Boolean = stateMachines.remove(stateMachine)

  override def persist(): Unit = {
    // TODO #9 store on the disk
  }

  override def load(): Unit = {
    // TODO #9 Load from the disk
  }
}
