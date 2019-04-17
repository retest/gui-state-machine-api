package de.retest.guistatemachine.api.example

import java.util.Arrays

import de.retest.guistatemachine.api.GuiStateMachineApi
import de.retest.recheck.ui.descriptors._
import de.retest.recheck.ui.image.Screenshot
import de.retest.surili.commons.actions.NavigateToAction

// TODO #19 Replace this example with unit tests when everything works.
object Example extends App {
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val rootElementC = getRootElement("c", 0)
  private val action0 = new NavigateToAction("http://google.com")
  private val action1 = new NavigateToAction("http://wikipedia.org")

  val api = GuiStateMachineApi.neo4jEmbedded
  val stateMachine = api.getStateMachine("tmp") match {
    case Some(s) => s

    case None => api.createStateMachine("tmp")
  }

  println(s"All states before clearing: ${stateMachine.getAllStates.size}")

  stateMachine.clear()

  println(s"All states after clearing: ${stateMachine.getAllStates.size}")

  val startSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
  val endSutState = new SutState(Arrays.asList(rootElementA))

  stateMachine.getState(startSutState)

  println(s"All states after adding start state: ${stateMachine.getAllStates.size}")

  stateMachine.getState(endSutState)

  println(s"All states after adding end state: ${stateMachine.getAllStates.size}")

  val startStateTmp = stateMachine.getState(startSutState)

  stateMachine.executeAction(startSutState, action0, endSutState)
  stateMachine.executeAction(startSutState, action1, endSutState)

  val startState = stateMachine.getState(startSutState)
  val numberOfOutgoingActionTransitions = startState.getOutgoingActionTransitions.size
  println(s"Number of outgoing action transitions: $numberOfOutgoingActionTransitions")

  val endState = stateMachine.getState(endSutState)
  val numberOfIncomingActionTransitions = endState.getIncomingActionTransitions.size
  println(s"Number of incoming action transitions: $numberOfOutgoingActionTransitions")

  println(s"All states after executing action0: ${stateMachine.getAllStates.size}")

  /**
    * Creates a new identifying attributes collection which should only match other identifying attributes with the same ID.
    *
    * @param id The ID is used as value for different attributes.
    * @return The identifying attributes.
    */
  def getIdentifyingAttributes(id: String): IdentifyingAttributes =
    new IdentifyingAttributes(Arrays.asList(new StringAttribute("a", id), new StringAttribute("b", id), new StringAttribute("c", id)))

  /**
    * The identifying attributes and the contained components specify the equality.
    *
    * @param id  This value is a criteria for equality of the returned element.
    * @param numberOfContainedComponents This value is a criteria for equality of the returned element.
    * @return A new root element which is equal to itself but not to any other root element.
    */
  def getRootElement(id: String, numberOfContainedComponents: Int): RootElement = {
    val r = new RootElement(
      "retestId",
      getIdentifyingAttributes(id),
      new Attributes(),
      new Screenshot("prefix", Array(1, 2, 3), Screenshot.ImageType.PNG),
      "screen0",
      0,
      "My Window"
    )
    if (numberOfContainedComponents > 0) {
      r.addChildren(scala.collection.JavaConversions.seqAsJavaList[Element](0 to numberOfContainedComponents map { _ =>
        getRootElement("x", 0)
      }))
    }
    r
  }
}
