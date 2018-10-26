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
A test suite is a set of test cases.

### Test Case
A test case is a sequence of UI actions.

### UI Action
A UI action is an action which can be triggert by the user via the GUI.

### UI Path
A UI path is a sequence of states with transitions from one state to another.
Each transition is a UI action.

### State
A state is defined by the set of all visible and interactable windows together with their enabled widgets.

## REST API
Some suggestions on how the REST API could look like:

* `/applications` GET queries all registered GUI applications
* `/create-application` POST registers a new GUI application
* `/application/<long>` GET queries a registered GUI application
* `/application/<long>/test-suites` GET queries all test suites for an existing GUI application
* `/application/<long>/create-test-suite` POST registers a new test suite for an existing GUI application
* `/application/<long>/test-suite/<long>` GET queries a registered test suite for an existing GUI application