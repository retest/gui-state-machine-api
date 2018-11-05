package de.retest.guistatemachine.dsl

import scala.collection.immutable.HashMap

object StateMachines {
  def apply(f: => StateMachine): Seq[StateMachine] = {
    def constructSeq(s: StateMachine, seq: Seq[StateMachine]): Seq[StateMachine] =
      if (s.previous ne null) constructSeq(s.previous, Seq(s) ++ seq) else Seq(s) ++ seq

    constructSeq(f, Seq())
  }

  def toModel(s: Seq[StateMachine]): de.retest.guistatemachine.model.StateMachines = {
    val hashmap = new HashMap[de.retest.guistatemachine.model.Id, de.retest.guistatemachine.model.StateMachine]()

    // TODO Implement conversion from the DSL to the model.
    /*
    s.foreach(x => {
      de.retest.guistatemachine.model.StateMachine
    })
    */

    de.retest.guistatemachine.model.StateMachines(de.retest.guistatemachine.model.Map[de.retest.guistatemachine.model.StateMachine](hashmap))
  }
}