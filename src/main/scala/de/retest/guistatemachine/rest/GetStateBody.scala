package de.retest.guistatemachine.rest

import de.retest.guistatemachine.api.{Action, Descriptors}

case class GetStateBody(descriptors: Descriptors, neverExploredActions: Set[Action])
