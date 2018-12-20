package de.retest.guistatemachine.rest

import de.retest.guistatemachine.api.{Descriptors, State}
import de.retest.surili.model.Action

case class ExecuteActionBody(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action])
