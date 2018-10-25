package de.retest.guistatemachine.model

final case class TestSuites(var values: scala.collection.immutable.Map[Id, TestSuite]) extends Map[TestSuite](values)