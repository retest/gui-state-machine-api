package de.retest.guistatemachine.api.neo4j

import de.retest.recheck.ui.descriptors.{Element, SutState}
import de.retest.surili.commons.actions._
import org.neo4j.ogm.typeconversion.AttributeConverter

import scala.xml._

/**
  * We do not want to store the whole target element as XML again. Hence, we store its retest ID which is unique and
  * matches the element in the SUT state and only the additional attributes.
  */
class ActionConverter(val sutState: Option[SutState]) extends AttributeConverter[Action, String] {

  def this() = this(None)

  def toGraphProperty(value: Action): String = {
    val nodeBuffer = new NodeBuffer

    nodeBuffer += <type>{value.getClass.getSimpleName}</type>

    if (value.getTargetElement.isPresent) {
      val retestId = value.getTargetElement.get().getRetestId
      nodeBuffer += <retestId>{retestId}</retestId>
    }

    value match {
      case a: ChangeValueOfAction =>
        val sequences = a.getKeysToSend map { sequence =>
          <sequence>{sequence.toString}</sequence>
        }
        nodeBuffer += <keys>{sequences}</keys>
      case a: NavigateToAction =>
        nodeBuffer += <url>{a.getUrl}</url>
      case a: SwitchToWindowAction =>
        nodeBuffer += <window>{a.getWindowName}</window>
      case _ =>
    }

    val topLevelNode = <action>{nodeBuffer}</action>
    val stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
    val prettyPrinter = new PrettyPrinter(0, 2)
    prettyPrinter.formatNodes(topLevelNode, TopScope, stringBuilder)
    stringBuilder.toString()
  }
  def toEntityAttribute(value: String): Action = sutState match {
    case Some(state) =>
      val node = scala.xml.XML.loadString(value)
      val typeNode = getNodeByTag(node, "type")
      typeNode.text match {
        case "ChangeValueOfAction" =>
          val element = getElement(node, state)
          val keys = getNodeByTag(node, "keys")
          val sequences = keys.child map { c =>
            c.text
          }
          new ChangeValueOfAction(element, sequences.toArray)
        case "ClickOnAction" =>
          val element = getElement(node, state)
          new ClickOnAction(element)
        case "NavigateToAction" =>
          val urlNode = getNodeByTag(node, "url")
          new NavigateToAction(urlNode.text)
        case "NavigateBackAction"    => new NavigateBackAction
        case "NavigateForwardAction" => new NavigateForwardAction
        case "NavigateRefreshAction" => new NavigateRefreshAction
        case "SwitchToWindowAction" =>
          val windowNode = getNodeByTag(node, "window")
          new SwitchToWindowAction(windowNode.text)
        case _ => throw new RuntimeException("Unknown type.")
      }

    case None => throw new RuntimeException("We need the SutState to reconstruct the action")
  }

  private def getNodeByTag(node: Node, tag: String): Node = {
    val matchingNodes = node \\ tag
    if (matchingNodes.isEmpty) {
      throw new RuntimeException(s"Missing node with tag $tag.")
    } else {
      matchingNodes.head
    }
  }

  private def getElement(node: Node, sutState: SutState): Element = {
    val retestId = getNodeByTag(node, "retestId").text
    getElementByRetestId(retestId, sutState) match {
      case Some(element) => element
      case None          => throw new RuntimeException(s"Missing element with retestId $retestId")
    }
  }

  private def getElementByRetestId(retestId: String, sutState: SutState): Option[Element] = {
    val elements = scala.collection.mutable.Set[Element]()
    val iterator = sutState.getRootElements.iterator()
    var result: Option[Element] = None

    while (iterator.hasNext && result.isEmpty) {
      val element = iterator.next()
      result = if (element.getRetestId == retestId) {
        Some(element)
      } else { None }

      val nestedIterator = element.getContainedElements.iterator()

      while (nestedIterator.hasNext) {
        elements += nestedIterator.next()
      }
    }

    result
  }
}
