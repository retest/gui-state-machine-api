package de.retest.guistatemachine.dsl

import org.scalatest.WordSpec
import org.scalatest.Matchers

/**
 * Tests the construction of state machines with the DSL.
 */
class StateMachinesSpec extends WordSpec with Matchers {

  "StateMachines" should {
    "be constructed as NFAs from objects" in {
      case object Start0 extends InitialState
      case object S0_0 extends State
      case object S0_1 extends State
      case object End0 extends FinalState

      case object Start1 extends InitialState
      case object S1_0 extends State
      case object S1_1 extends State
      case object End1 extends FinalState

      case object EnterText extends Action
      case object PressExitButton extends Action

      val r = StateMachines {
        StateMachine {
          Start0 - EnterText - S0_0
          Start0 - EnterText - S0_1
          S0_0 - PressExitButton - End0
          S0_1 - PressExitButton - End0
        } ~
          StateMachine {
            Start1 - EnterText - S1_0
            Start1 - EnterText - S1_1
            S1_0 - PressExitButton - End1
            S1_1 - PressExitButton - End1
          }
      }

      r.size shouldEqual 2
      val stateMachine0 = r(0)

      val initial0 = stateMachine0.getInitial
      initial0 should be theSameInstanceAs Start0
      initial0.getTransitions.size shouldEqual 2

      val initial0Trans0 = initial0.getTransitions(0)
      initial0Trans0.getAction should be theSameInstanceAs EnterText
      initial0Trans0.getTo should be theSameInstanceAs S0_0

      val initial0Trans1 = initial0.getTransitions(1)
      initial0Trans1.getAction should be theSameInstanceAs EnterText
      initial0Trans1.getTo should be theSameInstanceAs S0_1
    }
  }
}