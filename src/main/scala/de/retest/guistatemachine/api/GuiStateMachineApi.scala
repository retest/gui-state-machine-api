package de.retest.guistatemachine.api

/**
  * This API allows the creation, modification and deletion of state machines ([[GuiStateMachine]]) which are created
  * during test generations with the help of Genetic Algorithms.
  * To store the state machines permanently, you have to call [[GuiStateMachineApi.save]] manually.
  * Otherwise, they will only be stored in the memory.
  * [[GuiStateMachineApi.load]] allows loading state machines from a file.
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

  /**
    * Stores all state machines on the disk.
    * Persistence can be useful when the state machines become quite big and the generation/modification is interrupted
    * and continued later.
    *
    * @param filePath The file which the state machines are stored into.
    */
  def save(filePath: String): Unit

  /**
    * Clears all current state machines and loads all state machines from the disk.
    *
    * @param filePath The file which the state machines are loaded from.
    */
  def load(filePath: String): Unit
}
