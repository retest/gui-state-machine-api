package de.retest.guistatemachine.api

trait GuiStateMachineApi {

  /**
    * Creates a new [[GuiStateMachine]].
    * @return The new GUI state machine.
    */
  def createStateMachine: GuiStateMachine

  /**
    * Removes a persisted [[GuiStateMachine]].
    * @param stateMachine The persisted GUI state machine.
    * @return True if it has been persisted before and is no remove. Otherwise, false.
    */
  def removeStateMachine(stateMachine: GuiStateMachine): Boolean
}