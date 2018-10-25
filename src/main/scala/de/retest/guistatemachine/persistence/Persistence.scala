package de.retest.guistatemachine.persistence

import scala.collection.immutable.HashMap

import de.retest.guistatemachine.model.GuiApplication
import de.retest.guistatemachine.model.GuiApplications
import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.TestSuite
import de.retest.guistatemachine.model.TestSuites
import de.retest.guistatemachine.model.Map

/**
 * Allows concurrent access to the persistence of the resources.
 * The actual persistence layer is hidden by this class.
 */
class Persistence {
  // database
  private val guiApplications = GuiApplications(new HashMap[Id, GuiApplication])

  def getApplications(): GuiApplications = guiApplications

  def addApplication(): Id = {
    val apps = guiApplications
    apps.synchronized {
      val id = apps.generateId
      apps.setValues(apps.getValues + (id -> new GuiApplication(TestSuites(new HashMap[Id, TestSuite]))))
      id
    }
  }
  def getApplication(id: Id): Option[GuiApplication] = {
    val apps = guiApplications
    apps.synchronized {
      if (apps.values.contains(id)) Some(apps.values(id)) else { None }
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
          val id = testSuites.generateId
          testSuites.setValues(testSuites.getValues + (id -> TestSuite()))
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
          if (testSuites.values.contains(testSuiteId)) Some(testSuites.values(testSuiteId)) else None
        }
      }
      case None => None
    }
  }
}