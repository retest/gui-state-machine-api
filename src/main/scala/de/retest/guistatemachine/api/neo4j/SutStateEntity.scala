package de.retest.guistatemachine.api.neo4j

import de.retest.recheck.ui.descriptors.SutState
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.neo4j.ogm.annotation.{GeneratedValue, Id, _}

// TODO #19 Use this entity and sessions instead of manual transactions.
@NodeEntity
class SutStateEntity(state: SutState) extends Entity {

  def this() = this(null)

  @Id @GeneratedValue private val id = 0L

  @Convert(classOf[SutStateConverter])
  private val sutState = state
}
