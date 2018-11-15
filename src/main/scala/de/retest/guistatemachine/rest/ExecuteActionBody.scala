package de.retest.guistatemachine.rest

import de.retest.guistatemachine.api.{Action, Descriptors, State}

case class ExecuteActionBody(from: State, a: Action, descriptors: Descriptors, neverExploredActions: Set[Action])
