package de.retest.guistatemachine.model

final case class Id(val id: Long) extends Ordered[Id] {
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: Id): Int = this.id compare that.id
}