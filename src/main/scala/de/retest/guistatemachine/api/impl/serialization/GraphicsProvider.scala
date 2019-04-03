package de.retest.guistatemachine.api.impl.serialization

import java.awt.Color

import com.github.systemdir.gml.model.{EdgeGraphicDefinition, GraphicDefinition, NodeGraphicDefinition, YedGmlGraphicsProvider}
import de.retest.guistatemachine.api.SutStateIdentifier

class GraphicsProvider extends YedGmlGraphicsProvider[SutStateIdentifier, GraphActionEdge, AnyRef] {
  override def getVertexGraphics(vertex: SutStateIdentifier): NodeGraphicDefinition =
    new NodeGraphicDefinition.Builder().setFill(Color.LIGHT_GRAY).setLineColor(Color.black).setFontStyle(GraphicDefinition.FontStyle.ITALIC).build
  override def getEdgeGraphics(edge: GraphActionEdge, edgeSource: SutStateIdentifier, edgeTarget: SutStateIdentifier): EdgeGraphicDefinition =
    new EdgeGraphicDefinition.Builder()
      .setTargetArrow(EdgeGraphicDefinition.ArrowType.SHORT_ARROW)
      .setLineType(GraphicDefinition.LineType.DASHED)
      .build
  override def getGroupGraphics(group: AnyRef, groupElements: java.util.Set[SutStateIdentifier]): NodeGraphicDefinition = null
}
