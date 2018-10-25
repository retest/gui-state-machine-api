package de.retest.guistatemachine.model

final case class GuiApplications(var values: scala.collection.immutable.Map[Id, GuiApplication]) extends Map[GuiApplication](values)