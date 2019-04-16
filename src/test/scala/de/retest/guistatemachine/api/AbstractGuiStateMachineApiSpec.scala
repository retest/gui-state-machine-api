package de.retest.guistatemachine.api

import java.io.File

import de.retest.surili.commons.actions.NavigateToAction
import org.scalatest.BeforeAndAfterEach

abstract class AbstractGuiStateMachineApiSpec extends AbstractApiSpec with BeforeAndAfterEach {
  def getName: String
  def getCut: GuiStateMachineApi
  private var cut: GuiStateMachineApi = null

  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val rootElementC = getRootElement("c", 0)
  private val action0 = new NavigateToAction("http://google.com")
  private val action0Identifier = new ActionIdentifier(action0)
  private val action1 = new NavigateToAction("http://wikipedia.org")
  private val action1Identifier = new ActionIdentifier(action1)

  override def beforeEach() {
    cut = getCut
    cut.clear()
  }

  getName should {
    "create, get and remove a new state machine" in {
      cut.createStateMachine("tmp")
      val stateMachine = cut.getStateMachine("tmp")
      stateMachine.isDefined shouldBe true
      cut.removeStateMachine("tmp") shouldBe true
      cut.getStateMachine("tmp").isDefined shouldBe false
    }

    "clear all state machines" in {
      cut.createStateMachine("tmpX")
      cut.createStateMachine("tmpY")
      cut.createStateMachine("tmpZ")
      cut.clear()
      cut.getStateMachine("tmpX").isEmpty shouldEqual true
      cut.getStateMachine("tmpY").isEmpty shouldEqual true
      cut.getStateMachine("tmpZ").isEmpty shouldEqual true
    }

    "not create a new state when using the same root elements" in {
      val s0 = createSutState(getRootElement("a", 1))
      val s0Equal = createSutState(getRootElement("a", 1))
      val differentState = createSutState(getRootElement("a", 2))
      s0.equals(s0Equal) shouldBe true
      s0.hashCode() shouldEqual s0Equal.hashCode()
      differentState.equals(s0) shouldBe false
      differentState.hashCode() should not equal s0.hashCode()
      val stateMachine = cut.createStateMachine("tmp")
      stateMachine.getAllStates.size shouldEqual 0
      stateMachine.getState(s0)
      stateMachine.getAllStates.size shouldEqual 1
      stateMachine.getState(s0Equal)
      stateMachine.getAllStates.size shouldEqual 1
      stateMachine.getState(differentState)
      stateMachine.getAllStates.size shouldEqual 2
    }

    "add two transitions to two new states for the same action and two transitions for the same action to another state" in {
      val stateMachine = cut.createStateMachine("tmp")
      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val initial = stateMachine.getState(initialSutState)

      // execute action0 for the first time
      val s0SutState = createSutState(rootElementA)
      val s0 = stateMachine.getState(s0SutState)
      stateMachine.executeAction(initialSutState, action0, s0SutState)
      initial.getOutgoingActionTransitions.size shouldEqual 1
      initial.getOutgoingActionTransitions(action0Identifier).states.size shouldEqual 1
      initial.getIncomingActionTransitions.size shouldEqual 0
      s0.getOutgoingActionTransitions.size shouldEqual 0
      s0.getIncomingActionTransitions.size shouldEqual 1
      s0.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1

      // execute action0 for the second time
      val s1SutState = createSutState(rootElementB)
      val s1 = stateMachine.getState(s1SutState)
      stateMachine.executeAction(initialSutState, action0, s1SutState)
      initial.getOutgoingActionTransitions.size shouldEqual 1
      initial.getOutgoingActionTransitions(action0Identifier).states.size shouldEqual 2
      initial.getIncomingActionTransitions.size shouldEqual 0
      s1.getOutgoingActionTransitions.size shouldEqual 0
      s1.getIncomingActionTransitions.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1

      // execute action1 for the first time
      val s2SutState = createSutState(rootElementC)
      val s2 = stateMachine.getState(s2SutState)
      stateMachine.executeAction(initialSutState, action1, s2SutState)
      initial.getOutgoingActionTransitions.size shouldEqual 2
      initial.getOutgoingActionTransitions(action1Identifier).states.size shouldEqual 1
      initial.getIncomingActionTransitions.size shouldEqual 0
      s2.getOutgoingActionTransitions.size shouldEqual 0
      s2.getIncomingActionTransitions.size shouldEqual 1
      s2.getIncomingActionTransitions(action1Identifier).states.size shouldEqual 1

      // execute action1 for the second time but from s1SutState to create one incoming action from two different states
      stateMachine.executeAction(s1SutState, action1, s2SutState)
      s1.getOutgoingActionTransitions.size shouldEqual 1
      s1.getOutgoingActionTransitions(action1Identifier).states.size shouldEqual 1
      s1.getIncomingActionTransitions.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1
      s2.getOutgoingActionTransitions.size shouldEqual 0
      s2.getIncomingActionTransitions.size shouldEqual 1
      s2.getIncomingActionTransitions(action1Identifier).states shouldEqual Set(initial, s1)

      stateMachine.clear()
      stateMachine.getAllStates.isEmpty shouldEqual true
    }

    "store a state for the second access" in {
      val stateMachine = cut.createStateMachine("tmp")
      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val initialFromAccess0 = stateMachine.getState(initialSutState)
      val initialFromAccess1 = stateMachine.getState(initialSutState)
      initialFromAccess0 shouldEqual initialFromAccess1
    }

    "save GML " in {
      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val finalSutState = createSutState(rootElementC)

      // Create the whole state machine:
      val guiStateMachine = cut.createStateMachine("tmp")
      guiStateMachine.executeAction(initialSutState, action0, finalSutState)
      guiStateMachine.executeAction(initialSutState, action1, finalSutState)
      guiStateMachine.executeAction(finalSutState, action0, initialSutState)
      guiStateMachine.executeAction(finalSutState, action1, initialSutState)

      val filePath = "./target/test_state_machine.gml"
      val oldFile = new File(filePath)

      if (oldFile.exists()) { oldFile.delete() } shouldEqual true

      GuiStateMachineSerializer.gml(guiStateMachine).save(filePath)

      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      val source = scala.io.Source.fromFile(filePath)
      val lines = source.mkString
      lines shouldEqual
        """Creator "JGraphT GML Exporter - modified by Hayato Hess, Andreas Hofstadler"
          |Version 1
          |graph
          |[
          |	label ""
          |	directed 1
          |	node
          |	[
          |		id 1
          |		label "SutStateIdentifier[sutState=State[descriptor=[]], hash=acd05dfba59670825451169c470d430727226dd0dec48c64961305a0c5ab1ecb]"
          |		graphics
          |		[
          |			type	"rectangle"
          |			fill	"#c0c0c0ff"
          |			line	"#000000ff"
          |		]
          |		LabelGraphics
          |		[
          |			fontStyle	"ITALIC"
          |		]
          |	]
          |	node
          |	[
          |		id 2
          |		label "SutStateIdentifier[sutState=State[descriptor=[, , ]], hash=c44472d3d18e4f62b073a232e3119de9d94d3c6242b65125f454d62aced7f84e]"
          |		graphics
          |		[
          |			type	"rectangle"
          |			fill	"#c0c0c0ff"
          |			line	"#000000ff"
          |		]
          |		LabelGraphics
          |		[
          |			fontStyle	"ITALIC"
          |		]
          |	]
          |	edge
          |	[
          |		id 3
          |		source 1
          |		target 2
          |		label "ActionIdentifier[action=NavigateToAction(url=http://wikipedia.org), hash=240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084]"
          |		LabelGraphics
          |		[
          |			model	"centered"
          |			position	"center"
          |		]
          |		graphics
          |		[
          |			fill	"#000000ff"
          |			style	"DASHED"
          |			targetArrow	"short"
          |		]
          |	]
          |	edge
          |	[
          |		id 4
          |		source 1
          |		target 2
          |		label "ActionIdentifier[action=NavigateToAction(url=http://google.com), hash=fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f]"
          |		LabelGraphics
          |		[
          |			model	"centered"
          |			position	"center"
          |		]
          |		graphics
          |		[
          |			fill	"#000000ff"
          |			style	"DASHED"
          |			targetArrow	"short"
          |		]
          |	]
          |	edge
          |	[
          |		id 5
          |		source 2
          |		target 1
          |		label "ActionIdentifier[action=NavigateToAction(url=http://wikipedia.org), hash=240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084]"
          |		LabelGraphics
          |		[
          |			model	"centered"
          |			position	"center"
          |		]
          |		graphics
          |		[
          |			fill	"#000000ff"
          |			style	"DASHED"
          |			targetArrow	"short"
          |		]
          |	]
          |	edge
          |	[
          |		id 6
          |		source 2
          |		target 1
          |		label "ActionIdentifier[action=NavigateToAction(url=http://google.com), hash=fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f]"
          |		LabelGraphics
          |		[
          |			model	"centered"
          |			position	"center"
          |		]
          |		graphics
          |		[
          |			fill	"#000000ff"
          |			style	"DASHED"
          |			targetArrow	"short"
          |		]
          |	]
          |]
          |""".stripMargin

      source.close()
    }

    "load GML " in {
      val guiStateMachine = cut.createStateMachine("tmp")
      the[UnsupportedOperationException] thrownBy GuiStateMachineSerializer.gml(guiStateMachine).load("bla") should have message "Loading GML is not supported."
    }
  }
}
