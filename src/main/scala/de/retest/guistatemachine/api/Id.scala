package de.retest.guistatemachine.api

@SerialVersionUID(1L)
case class Id(id: Long) extends Ordered[Id] with Serializable {

  override def compare(that: Id): Int = this.id compare that.id
}
