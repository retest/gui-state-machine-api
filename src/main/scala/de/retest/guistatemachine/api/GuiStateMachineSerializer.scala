package de.retest.guistatemachine.api

import de.retest.guistatemachine.api.impl.serialization.{GuiStateMachinGMLSerializer, GuiStateMachineJavaObjectStreamSerializer}

trait GuiStateMachineSerializer {
  def save(filePath: String)
  def load(filePath: String)
}

object GuiStateMachineSerializer {
  def javaObjectStream(guiStateMachine: GuiStateMachine): GuiStateMachineSerializer = new GuiStateMachineJavaObjectStreamSerializer(guiStateMachine)
  def gml(guiStateMachine: GuiStateMachine): GuiStateMachineSerializer = new GuiStateMachinGMLSerializer(guiStateMachine)
}
