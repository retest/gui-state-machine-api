package de.retest.guistatemachine.furrermodel

/**
 * A visible window which the user can interact with.
 */
trait GuiWindow {
  /**
   * All visible widgets on the window.
   */
  private val widgets = Seq[GuiWidget]()
}