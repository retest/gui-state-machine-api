package de.retest.guistatemachine.api.neo4j

import org.neo4j.ogm.annotation.{GeneratedValue, Id}

abstract class Entity {
  @Id @GeneratedValue private val id = 0L
  def getId: Long = id
}
