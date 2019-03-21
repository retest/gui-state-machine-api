package de.retest.guistatemachine.api
import de.retest.guistatemachine.api.impl.GuiStateMachineApiImpl

/**
  * This API allows the creation, modification and deletion of state machines ([[GuiStateMachine]]) which are created
  * during test generations with the help of Genetic Algorithms.
  */
trait GuiStateMachineApi {

  /**
    * Creates a new [[GuiStateMachine]].
    *
    * @return The new GUI state machine.
    */
  def createStateMachine(): Id

  /**
    * Removes an existing [[GuiStateMachine]].
    *
    * @param id The ID of the GUI state machine.
    * @return True if it existed and was removed by this call. Otherwise, false.
    */
  def removeStateMachine(id: Id): Boolean

  /**
    * Gets an existing [[GuiStateMachine]].
    *
    * @param id The ID of the GUI state machine.
    * @return The existing GUI state machine or nothing.
    */
  def getStateMachine(id: Id): Option[GuiStateMachine]

  /**
    * Clears all state machines.
    */
  def clear(): Unit
}

object GuiStateMachineApi {
  private val impl = new GuiStateMachineApiImpl

  /**
    * @return The standard implementaiton of the API.
    */
  def apply(): GuiStateMachineApi = impl
}
