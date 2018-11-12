package de.retest.guistatemachine.rest

import de.retest.guistatemachine.rest.model.{Action, Actions, Id}
import org.scalatest.{Matchers, WordSpec}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.immutable.HashMap

class JsonFormatForIdMapSpec extends WordSpec with Matchers {
  implicit val idFormat = jsonFormat1(Id)

  implicit val actionFormat = jsonFormat0(Action)
  implicit val idMapFormatActions = new JsonFormatForIdMap[Action]
  implicit val actionsFormat = jsonFormat1(Actions)

  "The JSON format" should {
    "convert empty actions into JSON and back" in {
      val actions = model.Actions(model.Map(new HashMap[Id, Action]()))
      val json = actions.toJson
      json.toString shouldEqual "{\"actions\":{}}"
      val transformedActions = json.convertTo[Actions]
      transformedActions.actions.values.isEmpty shouldEqual true
    }

    "convert actions with elements into JSON and back" in {
      val actions = model.Actions(model.Map(new HashMap[Id, Action]()))
      actions.actions.values = actions.actions.values + (Id(0) -> Action())
      val json = actions.toJson
      json.toString shouldEqual "{\"actions\":{\"0\":{}}}"
      val transformedActions = json.convertTo[Actions]
      transformedActions.actions.values.isEmpty shouldEqual false
      transformedActions.actions.values.contains(Id(0)) shouldEqual true
    }
  }

}
