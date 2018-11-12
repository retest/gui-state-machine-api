package de.retest.guistatemachine.persistence

import de.retest.guistatemachine.rest.model.{Id, Map, StateMachine, StateMachines}

import scala.collection.immutable.HashMap

class Persistence {
  // database
  private val stateMachines = StateMachines(Map(new HashMap[Id, StateMachine]))

  def getStateMachines(): StateMachines = stateMachines

  def getStateMachine(id: Id): Option[StateMachine] = if (stateMachines.stateMachines.hasElement(id)) Some(stateMachines.stateMachines.getElement(id)) else None

  // TODO #4 Pass all unexplored actions for the initial state!
  def createStateMachine(): Id = stateMachines.stateMachines.addNewElement(StateMachine())

  def deleteStateMachine(id: Id): Boolean = stateMachines.stateMachines.removeElement(id)
}