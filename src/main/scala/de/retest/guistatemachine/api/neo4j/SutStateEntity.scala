package de.retest.guistatemachine.api.neo4j

import de.retest.recheck.ui.descriptors.SutState
import org.neo4j.ogm.annotation._
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
class SutStateEntity(state: SutState) extends Entity {

  def this() = this(null)

  @Index(unique = true)
  @Convert(classOf[SutStateConverter])
  val sutState: SutState = state
}
