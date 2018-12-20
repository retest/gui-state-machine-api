package de.retest.guistatemachine.rest

import de.retest.guistatemachine.api.Descriptors
import de.retest.surili.model.Action

case class GetStateBody(descriptors: Descriptors, neverExploredActions: Set[Action])
