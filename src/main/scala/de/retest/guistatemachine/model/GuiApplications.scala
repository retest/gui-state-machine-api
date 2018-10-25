package de.retest.guistatemachine.model

final case class GuiApplications(applications: Map[GuiApplication]) {
  // TODO Generate IDs in a better way. Maybe random numbers until one unused element is found?
  def generateId: Id = this.synchronized { if (applications.values.isEmpty) Id(0) else Id(applications.values.keySet.max.id + 1) }
}