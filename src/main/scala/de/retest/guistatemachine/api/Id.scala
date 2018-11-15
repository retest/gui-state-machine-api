package de.retest.guistatemachine.api

final case class Id(val id: Long) extends Ordered[Id] {

  override def compare(that: Id): Int = this.id compare that.id
}
