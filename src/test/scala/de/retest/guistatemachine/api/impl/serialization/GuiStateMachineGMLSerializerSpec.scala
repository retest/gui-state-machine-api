package de.retest.guistatemachine.api.impl.serialization

import java.io.File

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineSerializer}
import de.retest.surili.commons.test.TestUtil
import org.scalatest.BeforeAndAfterEach

class GuiStateMachineGMLSerializerSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val guiStateMachine = new GuiStateMachineImpl

  override def beforeEach() {
    guiStateMachine.clear()
  }

  "GuiStateMachineGMLSerializer" should {
    "save GML " in {
      val initialSutState = TestUtil.createSutState(rootElementA, rootElementB, rootElementC)
      val finalSutState = TestUtil.createSutState(rootElementC)

      // Create the whole state machine:
      val initialState = guiStateMachine.createState(initialSutState, unexploredActionTypes)
      val finalState = guiStateMachine.createState(finalSutState, unexploredActionTypes)
      guiStateMachine.executeAction(initialState, action0, finalState)
      guiStateMachine.executeAction(initialState, action1, finalState)
      guiStateMachine.executeAction(finalState, action0, initialState)
      guiStateMachine.executeAction(finalState, action1, initialState)

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
          |		label "SutStateIdentifier[sutState=State[descriptor=[, , ]], hash=d62af47d9844707082c557def0362d02483340412bd42465409a7773931eec29]"
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
          |		label "SutStateIdentifier[sutState=State[descriptor=[]], hash=c2b196629be15132ef89911058499b7f8153ede93355974d693f125240bdbe3e]"
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
      the[UnsupportedOperationException] thrownBy GuiStateMachineSerializer
        .gml(guiStateMachine)
        .load("bla") should have message "Loading GML is not supported."
    }
  }
}
