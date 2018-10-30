package de.retest.guistatemachine.dsl

import org.scalatest.WordSpec
import org.scalatest.Matchers

/**
 * Tests the construction of state machines with the DSL.
 */
class StateMachinesSpec extends WordSpec with Matchers {

  "StateMachines" should {
    "be constructed as NFAs from objects" in {
      case object Start extends InitialState
      case object S0 extends State
      case object S1 extends State
      case object End extends FinalState
      case object EnterText extends Action
      case object PressExitButton extends Action

      StateMachines {
        StateMachine {
          Start - EnterText - S0
          Start - EnterText - S1
          S0 - PressExitButton - End
          S1 - PressExitButton - End
        } ~
          StateMachine {
            Start - EnterText - S0
            Start - EnterText - S1
            S0 - PressExitButton - End
            S1 - PressExitButton - End
          }
      }
    }
  }
}