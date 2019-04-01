package de.retest.guistatemachine.api.impl.serialization
import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import com.github.systemdir.gml.YedGmlWriter
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineSerializer}
import de.retest.recheck.ui.descriptors.SutState
import org.jgrapht.graph.DirectedPseudograph

class GuiStateMachinGMLSerializer(guiStateMachine: GuiStateMachine) extends GuiStateMachineSerializer {

  type GraphType = DirectedPseudograph[SutState, GraphActionEdge]

  /**
    * Converts the state machines into GML which can be read by editors like yED.
    *
    * @param filePath The file which the GML data is stored into.
    * @throws RuntimeException If a vertex or edge cannot be added, this exception is thrown.
    */
  override def save(filePath: String): Unit = {
    // get graph from user
    val toDraw = createGraph()

    // define the look and feel of the graph
    val graphicsProvider = new GraphicsProvider

    // get the gml writer
    val writer =
      new YedGmlWriter.Builder[SutState, GraphActionEdge, AnyRef](graphicsProvider, YedGmlWriter.PRINT_LABELS: _*)
        .setEdgeLabelProvider(_.toString)
        .setVertexLabelProvider(sutState => "%s - hash code: %d".format(sutState.toString, sutState.hashCode()))
        .build

    // write to file
    val outputFile = new File(filePath)
    val output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))
    try { writer.export(output, toDraw) } finally { output.close() }
  }

  override def load(filePath: String): Unit = throw new UnsupportedOperationException("Loading GML is not supported.")

  private def createGraph(): GraphType = {
    val graph = new GraphType(classOf[GraphActionEdge])
    val allStatesSorted = guiStateMachine.getAllStates.toSeq.sortWith(hashCodeComparisonOfTuples)
    allStatesSorted.foreach { x =>
      val vertex = x._1
      if (!graph.addVertex(vertex)) { throw new RuntimeException(s"Failed to add vertex $vertex") }
    }

    allStatesSorted.foreach { x =>
      val fromVertex = x._1
      val allOutgoingActionTransitionsSorted = x._2.getOutgoingActionTransitions.toSeq.sortWith(hashCodeComparisonOfTuples)

      allOutgoingActionTransitionsSorted foreach { transition =>
        val actionTransitions = transition._2
        val action = transition._1
        actionTransitions.states.foreach { toState =>
          val toVertex = toState.getSutState
          val edge = GraphActionEdge(fromVertex, toVertex, action)
          if (!graph.addEdge(fromVertex, toVertex, edge)) { throw new RuntimeException(s"Failed to add edge $edge") }
        }
      }
    }
    graph
  }

  private def hashCodeComparisonOfTuples[A, B](a: (A, B), b: (A, B)) = a._1.hashCode().compareTo(b._2.hashCode()) < 0

}
