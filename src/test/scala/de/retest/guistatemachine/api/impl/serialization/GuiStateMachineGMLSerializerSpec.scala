package de.retest.guistatemachine.api.impl.serialization

import java.io.File

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineSerializer}
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

      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val finalSutState = createSutState(rootElementC)

      // Create the whole state machine:
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
          |		label "hash=acd05dfba59670825451169c470d430727226dd0dec48c64961305a0c5ab1ecb"
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
          |		label "hash=c44472d3d18e4f62b073a232e3119de9d94d3c6242b65125f454d62aced7f84e"
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
          |		label "hash=240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084"
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
          |		label "hash=fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f"
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
          |		label "hash=240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084"
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
          |		label "hash=fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f"
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
      the[UnsupportedOperationException] thrownBy GuiStateMachineSerializer.gml(guiStateMachine).load("bla") should have message "Loading GML is not supported."
    }
  }
}
