# GUI State Machine API

Service for the creation and modification of state machines of GUI tests based on a genetic algorithm.
This [REST](https://en.wikipedia.org/wiki/Representational_state_transfer) service stores the NFA in a [graph database](https://en.wikipedia.org/wiki/Graph_database).
The service hides the actual implementation and defines a fixed interface for calls.
Therefore, calling systems do not depend on the concrete implementation and it can be mocked easily for tests.

## Automatic Build with TravisCI
[![Build Status](https://travis-ci.org/retest/gui-state-machine-api.svg?branch=master)](https://travis-ci.org/retest/gui-state-machine-api)
[![Code Coverage](https://img.shields.io/codecov/c/github/retest/gui-state-machine-api/master.svg)](https://codecov.io/github/retest/gui-state-machine-api?branch=master)

## Manual Build
Use the command `sbt compile` to build the project manually.

## Manual Run
Use the command `sbt run` to start the REST service.

## Standalone Distribution
Use the command `sbt assembly` to create a standalone JAR which includes all dependencies including the Scala libraries.
The standalone JAR is generated as `target/scala-<scalaversion>/gui-state-machine-api-assembly-<version>.jar`.

## Model Notation
An [NFA](https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton) represents the states of the GUI.
The transitions are possible GUI actions.
The model is based on the UI model from [Search-Based System Testing: High Coverage, No False Alarms][http://www.specmate.org/papers/2012-07-Search-basedSystemTesting-HighCoverageNoFalseAlarms.pdf] (section "4.5 UI Model").
Whenever an unknown state is replaced by a newly discovered state, the model is updated.

Open questions:

* How is the state machine represented in the current version of retest?
* state machine != NFA != graph? It is definitely an NFA since multiple outoing transitions are allowed.
* REST -> HTTP what about the performance? Alternatively use a direct API in Scala? Should be possible from Java: <https://lampwww.epfl.ch/~michelou/scala/using-scala-from-java.html>
* What kind of analysises on the state machines are required except for the next possible actions?
* Whenever a new state is discovered, is the transition to the unknown state removed? Couldn't there be more states to be discovered? Doesn't matter?
* EFG model?
* How should the differences of the different versions of the state machines be stored?
* The demo of Retest allows recording and replaying tests. The same can be done with <https://www.seleniumhq.org/projects/ide/>, so benefits are the automatic test generation/training and the difference testing?

## Legacy code
The package `de.retest.graph` contains all legacy classes.
The package exists in version with GIT tag `retest-2.4.1`.

## Definitions
These definitions have been made by Furrer in his master's thesis.
See section `2.3.1 Problemrepräsentation` for a detailed definition of the SUT model.
The primary source for the definitions is [Exploring Realistic Program Behavior](https://www.st.cs.uni-
saarland.de/publications/files/fgross-tr-2012.pdf).

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

## APIs suggested in the project proposal of Surili
* [Selenium WebDriver](http://seleniumhq.org/docs/03_webdriver.jsp)
* [Gherkin](https://github.com/cucumber/cucumber/wiki/Gherkin)

## REST Frameworks for Scala
* Good example of a REST service with Akka: <https://github.com/ArchDev/akka-http-rest>
* <https://doc.akka.io/docs/akka-http/current/>
* <https://www.playframework.com/>
* <https://www.reddit.com/r/scala/comments/6izqac/akka_http_vs_play_ws_what_is_the_current_state/>
* <https://slides.com/leonardoregnier/deck#/>
* <https://nordicapis.com/8-frameworks-to-build-a-web-api-in-scala/>
* Akk HTTP test examples: <https://github.com/akka/akka-http/tree/master/akka-http-tests/src/test/scala/akka/http/scaladsl/server/directives>
* Replaced by Akka HTTP: <http://spray.io/>

If REST is too slow, Scala can still be called directly via Java.

## NFA Frameworks
TODO Add some.

## Graph Databases
[List of graph databases](https://en.wikipedia.org/wiki/Graph_database#List_of_graph_databases)

### Neo4j
* https://neo4j.com/ ([Licensing](https://neo4j.com/licensing/))
* https://github.com/neo4j/neo4j
* https://github.com/FaKod/neo4j-scala/ (Scala Wrapper)
* https://github.com/seancheatham/scala-graph
* https://neo4j.com/blog/neo4j-scala-introduction/

### TinkerPop3
* http://tinkerpop.apache.org/
* https://github.com/apache/tinkerpop
* https://users.scala-lang.org/t/scala-graph-combined-with-a-graph-db/2824
* https://github.com/mpollmeier/gremlin-scala

## Automatic Test Generation
* [EvoSuite](http://www.evosuite.org/)
* [Randoop](https://randoop.github.io/randoop/)
* [Guitar](https://sourceforge.net/projects/guitar/)

## Literature
* [Search-Based System Testing: High Coverage, No False Alarms](https://dl.acm.org/citation.cfm?id=2336762)
* Machine Learning and Evolutionary Computing for GUI-based Regression Testing, Master's Thesis by Daniel Kraus
* Vergleichsstudie maschineller Testverfahren für GUI-basierte Systeme, Master's Thesis by Felix Furrer