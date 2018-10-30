package de.retest.guistatemachine.persistence

import scala.collection.immutable.HashMap

import de.retest.guistatemachine.model.GuiApplication
import de.retest.guistatemachine.model.GuiApplications
import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.TestSuite
import de.retest.guistatemachine.model.TestSuites
import de.retest.guistatemachine.model.Map
import de.retest.guistatemachine.model.StateMachines
import de.retest.guistatemachine.model.StateMachine

/**
 * Allows concurrent access to the persistence of the resources.
 * The actual persistence layer is hidden by this class.
 */
class Persistence {
  // database
  private val stateMachines = StateMachines(Map(new HashMap[Id, StateMachine]))
  private val guiApplications = GuiApplications(Map(new HashMap[Id, GuiApplication]))

  def getStateMachines(): StateMachines = stateMachines

  def getApplications(): GuiApplications = guiApplications

  def addApplication(): Id = {
    val apps = guiApplications
    apps.synchronized {
      val id = apps.apps.generateId
      apps.apps.values = apps.apps.values + (id -> GuiApplication(TestSuites(Map(new HashMap[Id, TestSuite]))))
      id
    }
  }
  def getApplication(id: Id): Option[GuiApplication] = {
    val apps = guiApplications
    apps.synchronized {
      if (apps.apps.values.contains(id)) Some(apps.apps.values(id)) else { None }
    }
  }

  def deleteApplication(id: Id): Boolean = {
    val apps = guiApplications
    apps.synchronized {
      if (apps.apps.values.contains(id)) {
        apps.apps.values = apps.apps.values - id
        true
      } else {
        false
      }
    }
  }

  def getTestSuites(applicationId: Id): Option[TestSuites] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => Some(x.testSuites)
      case None => None
    }
  }

  def addTestSuite(applicationId: Id): Option[Id] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => {
        val testSuites = x.testSuites
        testSuites.synchronized {
          val id = testSuites.suites.generateId
          testSuites.suites.values = testSuites.suites.values + (id -> TestSuite())
          Some(id)
        }
      }
      case None => None
    }
  }
  def getTestSuite(applicationId: Id, testSuiteId: Id): Option[TestSuite] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => {
        val testSuites = x.testSuites
        testSuites.synchronized {
          if (testSuites.suites.values.contains(testSuiteId)) Some(testSuites.suites.values(testSuiteId)) else None
        }
      }
      case None => None
    }
  }

  def deleteTestSuite(applicationId: Id, testSuiteId: Id): Boolean = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => {
        val testSuites = x.testSuites
        testSuites.synchronized {
          if (testSuites.suites.values.contains(testSuiteId)) {
            testSuites.suites.values = testSuites.suites.values - testSuiteId
            true
          } else {
            false
          }
        }
      }
      case None => false
    }
  }
}