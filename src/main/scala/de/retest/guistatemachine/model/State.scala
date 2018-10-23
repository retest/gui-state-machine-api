package de.retest.guistatemachine.model

class State {
  private val windows = Set[GuiWindow]()
  /**
   * Actions which can be executed by the user in this state.
   */
  private val availableActions = Set[UIAction]()
}