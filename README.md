# GUI State Machine API

REST service for the creation and modification of nondeterministic finite automaton of GUI tests based on a genetic algorithm.
The service hides the actual implementation and defines a fixed interface for calls.
Therefore, calling systems do not depend on the concrete implementation and it can be mocked easily for tests.

## Automatic Build with TravisCI
[![Build Status](https://travis-ci.org/retest/gui-state-machine-api.svg?branch=master)](https://travis-ci.org/retest/gui-state-machine-api)
[![Code Coverage](https://img.shields.io/codecov/c/github/retest/gui-state-machine-api/master.svg)](https://codecov.io/github/retest/gui-state-machine-api?branch=master)

## SBT Commands
* `sbt compile` to build the project manually.
* `sbt run` to start the REST service.
* `sbt assembly` to create a standalone JAR which includes all dependencies including the Scala libraries. The standalone JAR is generated as `target/scala-<scalaversion>/gui-state-machine-api-assembly-<version>.jar`.
* `sbt eclipse` to generate a project for Eclipse.
* `sbt test` to execute all unit tests.
* `sbt coverage` to generate coverage data.
* `sbt coverageReport` to generate a HTML coverage report.
* `sbt scalastyle` to make a check with ScalaStyle.
* `sbt doc` to generate the scaladoc API documentation.

## Bash Scripts for REST Calls
The directory [scripts](./scripts) contains a number of Bash scripts which use `curl` to send REST calls to a running server.

## NFA for the Representation of Tests
A nondeterministic finite automaton represents the states of the GUI during the test.
The actions executed by the user on the widgets are the transitions.
If an action has not been executed, it leads to an unknown state.
The NFA is based on the UI model from [Search-Based System Testing: High Coverage, No False Alarms](http://www.specmate.org/papers/2012-07-Search-basedSystemTesting-HighCoverageNoFalseAlarms.pdf) (section "4.5 UI Model").
Whenever an unknown state is replaced by a newly discovered state, the NFA has to be updated.

**At the moment, the following definitions are incomplete and must be adapted to the actual implementation which calls this service.**

### Test Suite
A set of test cases.

### Test Case
A sequence of UI actions.

### UI Action
An action which can be triggered by the user via the GUI.

### UI Path
A sequence of states with transitions from one state to another.
Each transition is a UI action.

### State
A state is defined by the set of all visible and interactable windows together with their enabled widgets.

## DSL
There is a DSL to construct an NFA with GUI actions manually.
The package [dsl](./src/main/scala/de/retest/guistatemachine/dsl/).

The following example shows how to construct an NFA in Scala:
```scala
case object Start extends InitialState
case object S0 extends State
case object S1 extends State
case object End extends FinalState
case object EnterText extends Action
case object PressExitButton extends Action

StateMachines {
  StateMachine {
    Start - EnterText - S0
    Start - EnterText - S1
    S0 - PressExitButton - End
    S1 - PressExitButton - End
  }
}
```

## REST API
Some suggestions how the REST API for the state machine could look like:
* `/state-machines` GET queries all existing state machines.
* `/create-state-machine` POST creates a new state machine.
* `/state-machine/<long>` GET queries an existing state machine.
* `/state-machine/<long>/states` GET queries all existing states of the state machine.
* `/state-machine/<long>/state/<long>` GET queries a specific state of the state machine which contains transitions.
* `/state-machine/<long>/state/<long>/transitions` GET queries all transitions of a specific state.
* `/state-machine/<long>/state/<long>/transition/<long>` GET queries a specific transition of a specific state.
* `/state-machine/<long>/execute` POST executes the passed action from the passed state which might lead to a new state and adds a transition to the state machine. The action must be part of all actions?

Some suggestions on how the test representation REST API could look like (not necessarily required):

* `/applications` GET queries all existing GUI applications.
* `/create-application` POST creates a new GUI application.
* `/application/<long>` GET queries an existing GUI application.
* `/application/<long>` DELETE deletes an existing GUI application and all of its test suites etc.
* `/application/<long>/test-suites` GET queries all test suites for an existing GUI application.
* `/application/<long>/create-test-suite` POST creates a new test suite for an existing GUI application.
* `/application/<long>/test-suite/<long>` GET queries an existing test suite for an existing GUI application.
* `/application/<long>/test-suite/<long>` DELETE deletes an existing test suite for an existing GUI application.

## NFA Frameworks
This list contains frameworks for Scala which support the representation of an NFA:
* Akka FSM (FSM for actors): <https://doc.akka.io/docs/akka/current/fsm.html>
* Neo4J: <https://neo4j.com/>
* Gremlin-Scala: <https://github.com/mpollmeier/gremlin-scala>