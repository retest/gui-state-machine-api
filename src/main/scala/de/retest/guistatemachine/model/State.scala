package de.retest.guistatemachine.model

trait State {
  private val windows = Set[GuiWindow]()
  /**
   * Actions which can be executed by the user in this state.
   */
  private val availableActions = Set[UIAction]()
}