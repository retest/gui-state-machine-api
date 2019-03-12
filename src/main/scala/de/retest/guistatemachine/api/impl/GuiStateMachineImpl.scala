package de.retest.guistatemachine.api.impl

import java.io._

import com.github.systemdir.gml.YedGmlWriter
import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.jgrapht.graph.DirectedPseudograph

import scala.collection.immutable.{HashMap, HashSet}

@SerialVersionUID(1L)
class GuiStateMachineImpl extends GuiStateMachine with Serializable {
  @transient private val logger = Logger[GuiStateMachineImpl]
  // Make it accessible from the impl package for unit tests.
  private var states = new HashMap[SutState, State]

  /**
    * The legacy code stored execution counters for every action.
    */
  private var allExploredActions = new HashSet[Action]

  /**
    * `actionExecutionCounter` from the legacy code.
    * Stores the total number of executions per action.
    */
  private var actionExecutionTimes = new HashMap[Action, Int]

  override def getState(sutState: SutState): State = this.synchronized {
    if (states.contains(sutState)) {
      states(sutState)
    } else {
      logger.info(s"Create new state from SUT state with hash code ${sutState.hashCode()}")
      val s = new StateImpl(sutState)
      states += (sutState -> s)
      s
    }
  }

  override def executeAction(from: State, a: Action, to: State): State = this.synchronized {
    allExploredActions += a
    val old = actionExecutionTimes.get(a)
    old match {
      case Some(o) => actionExecutionTimes += (a -> (o + 1))
      case None    => actionExecutionTimes += (a -> 1)
    }
    from.addTransition(a, to)
    to
  }

  override def executeAction(fromSutState: SutState, a: Action, toSutState: SutState): State = executeAction(StateImpl(fromSutState), a, StateImpl(toSutState))

  override def getAllStates: Map[SutState, State] = this.synchronized { states }

  override def getAllExploredActions: Set[Action] = this.synchronized { allExploredActions }

  override def getActionExecutionTimes: Map[Action, Int] = this.synchronized { actionExecutionTimes }

  override def clear(): Unit = this.synchronized {
    states = new HashMap[SutState, State]
    allExploredActions = new HashSet[Action]
    actionExecutionTimes = new HashMap[Action, Int]
  }

  override def save(filePath: String): Unit = this.synchronized {
    val oos = new ObjectOutputStream(new FileOutputStream(filePath))
    oos.writeObject(this)
    oos.close()
  }

  override def load(filePath: String): Unit = this.synchronized {
    clear()
    val ois = new ObjectInputStream(new FileInputStream(filePath))
    val readStateMachine = ois.readObject.asInstanceOf[GuiStateMachineImpl]
    ois.close()
    states = readStateMachine.states
    allExploredActions = readStateMachine.allExploredActions
    actionExecutionTimes = readStateMachine.actionExecutionTimes
  }

  type GraphType = DirectedPseudograph[SutState, GraphActionEdge]

  override def saveGML(filePath: String): Unit = this.synchronized {
    // get graph from user
    val toDraw = getGraph()

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
    try writer.export(output, toDraw)
    finally if (output != null) output.close()
  }

  private def getGraph(): GraphType = {
    val graph = new GraphType(classOf[GraphActionEdge])
    val allStatesSorted = getAllStates.toSeq.sortWith(hashCodeComparisonOfTuples)
    allStatesSorted.foreach { x =>
      val vertex = x._1
      if (!graph.addVertex(vertex)) throw new RuntimeException(s"Failed to add vertex $vertex")
    }

    allStatesSorted.foreach { x =>
      val fromVertex = x._1
      val allTransitionsSorted = x._2.getTransitions.toSeq.sortWith(hashCodeComparisonOfTuples)

      allTransitionsSorted foreach { transition =>
        val actionTransitions = transition._2
        val action = transition._1
        actionTransitions.to.foreach { toState =>
          val toVertex = toState.getSutState
          val edge = GraphActionEdge(fromVertex, toVertex, action)
          if (!graph.addEdge(fromVertex, toVertex, edge)) throw new RuntimeException(s"Failed to add edge $edge")
        }
      }
    }
    graph
  }

  private def hashCodeComparisonOfTuples[A, B](a: (A, B), b: (A, B)) = a._1.hashCode().compareTo(b._2.hashCode()) < 0
}
