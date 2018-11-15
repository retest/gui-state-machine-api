package de.retest.guistatemachine.rest.model

import io.swagger.annotations.{ApiModel, ApiModelProperty}

import scala.annotation.meta.field

@ApiModel(description = "A map of state machines")
final case class StateMachines(
    @(ApiModelProperty @field)(value = "A map of state machines")
    stateMachines: Map[StateMachine])
