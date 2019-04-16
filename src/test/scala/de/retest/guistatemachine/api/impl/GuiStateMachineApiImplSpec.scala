package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractGuiStateMachineApiSpec, GuiStateMachineApi}

class GuiStateMachineApiImplSpec extends AbstractGuiStateMachineApiSpec {
  override def getName: String = "GuiStateMachineApiImpl"
  override def getCut: GuiStateMachineApi = new GuiStateMachineApiImpl
}
