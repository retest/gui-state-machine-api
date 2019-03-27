# GUI State Machine API

[![Build Status](https://travis-ci.com/retest/gui-state-machine-api.svg?branch=master)](https://travis-ci.com/retest/gui-state-machine-api)
[![Code Coverage](https://img.shields.io/codecov/c/github/retest/gui-state-machine-api/master.svg)](https://codecov.io/github/retest/gui-state-machine-api?branch=master)

API for the creation and modification of incomplete state machines which represent the exploration of a GUI application.
The states represent the GUI elements and the transitions represent the GUI actions.

This is a small code example of creating a new state machine, adding two states connected with a transition and saving the state machine:

```scala
import de.retest.guistatemachine.api.GuiStateMachineApi
import de.retest.guistatemachine.api.GuiStateMachineSerializer
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.NavigateToAction

val stateMachine = GuiStateMachineApi().createStateMachine("test")
val currentState = new SutState(currentDescriptors)
val action = new NavigateToAction("http://google.com")
val nextState = new SutState(nextDescriptors)
stateMachine.executeAction(currentState, action, nextState)

GuiStateMachineSerializer.javaObjectStream(stateMachine).save("mystatemachine.ser")
GuiStateMachineSerializer.gml(stateMachine).save("mystatemachine.gml")
```

State machines can be saved as and loaded from files using Java object serialization/deserialization.
Besides, they can be saved as [GML](https://en.wikipedia.org/wiki/Graph_Modelling_Language) files which can be visualized by editors like [yEd](https://www.yworks.com/products/yed).

## Build Credentials

Define the Nexus password in the environment variable `TRAVIS_NEXUS_PW`.
Otherwise, the build will fail!

## SBT Commands

* `sbt compile` to build the project manually.
* `sbt eclipse` to generate a project for Eclipse.
* `sbt test` to execute all unit tests.
* `sbt coverage` to generate coverage data.
* `sbt coverageReport` to generate a HTML coverage report.
* `sbt scalastyle` to make a check with ScalaStyle.
* `sbt doc` to generate the scaladoc API documentation.
* `sbt scalafmt` to format the Scala source files with scalafmt.
* `sbt 'release cross with-defaults'` to create a release with a new version number which is added as tag. This command does also publish the artifacts.
* `sbt publish` publishes the artifacts in ReTest's Nexus. Requires a `$HOME/.sbt/.credentials` file with the correct credentials. This command can be useful to publish SNAPSHOT versions.

## NFA for the Representation of GUI Behavior

A nondeterministic finite automaton (NFA) represents the states of the GUI during testing.
The actions executed by the user on GUI elements are represented by transitions.
If an action has not been executed yet from a state, it leads to the so-called unknown state *s<sub>?</sub>*.
The unknown state is a special state from which all actions could be executed.
Whenever an action, which previously led to *s<sub>?</sub>*, is being executed and then leads to a newly discovered state, the NFA has to be updated.

The NFA is based on the UI model from ["Search-Based System Testing: High Coverage, No False Alarms"](http://www.specmate.org/papers/2012-07-Search-basedSystemTesting-HighCoverageNoFalseAlarms.pdf) (section "4.5 UI Model"). Originally, it has been used together with a genetic algorithm for search-based system testing, where it served two purposes:

1. Population initialization: to give precedence to unexplored actions.
2. Mutation: to repair test cases.

## Concurrency

The creation and modification of state machines should be thread-safe.

## Backends

There can be different backends which manage the state machine.

### Neo4J

This backend uses the GraphDB [Neo4J](https://neo4j.com/) (community edition) with an embedded database.
It uses [Neo4J-OGM](https://neo4j.com/docs/ogm-manual/current/) to map our types to the graph database.

Each state machine is represented by a separate graph database stored in a separate directory.
The nodes all have the property "sutState" which contains the corresponding SUT state serialized as XML.
The relationship types correspond to actions.
Each relation has the property "counter" which contains the execution counter of the action.
