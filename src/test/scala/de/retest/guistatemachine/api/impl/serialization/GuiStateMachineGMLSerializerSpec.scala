package de.retest.guistatemachine.api.impl.serialization

import java.io.File
import java.util.Arrays

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineSerializer}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.NavigateToAction
import org.scalatest.BeforeAndAfterEach

class GuiStateMachineGMLSerializerSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val guiStateMachine = new GuiStateMachineImpl

  override def beforeEach() {
    guiStateMachine.clear()
  }

  "GuiStateMachineGMLSerializer" should {
    "save GML " in {
      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
      val finalSutState = new SutState(Arrays.asList(rootElementC))

      // Create the whole state machine:
      val initialState = guiStateMachine.getState(initialSutState)
      val finalState = guiStateMachine.getState(finalSutState)
      guiStateMachine.executeAction(initialSutState, action0, finalSutState)
      guiStateMachine.executeAction(initialSutState, action1, finalSutState)
      guiStateMachine.executeAction(finalSutState, action0, initialSutState)
      guiStateMachine.executeAction(finalSutState, action1, initialSutState)

      initialState.getTransitions.size shouldEqual 2
      finalState.getTransitions.size shouldEqual 2

      val filePath = "./target/test_state_machine.gml"
      val oldFile = new File(filePath)

      if (oldFile.exists()) { oldFile.delete() } shouldEqual true

      GuiStateMachineSerializer.gml(guiStateMachine).save(filePath)

      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      val lines = scala.io.Source.fromFile(filePath).mkString
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
          |		label "State[descriptor=[]] - hash code: 9132415"
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
          |		label "State[descriptor=[, , ]] - hash code: 416617022"
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
          |		label "NavigateToAction(url=http://google.com)"
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
          |		label "NavigateToAction(url=http://wikipedia.org)"
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
          |		label "NavigateToAction(url=http://google.com)"
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
          |		label "NavigateToAction(url=http://wikipedia.org)"
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
    }

    "load GML " in {
      the[UnsupportedOperationException] thrownBy GuiStateMachineSerializer.gml(guiStateMachine).load("bla") should have message "Loading GML is not supported."
    }
  }
}
