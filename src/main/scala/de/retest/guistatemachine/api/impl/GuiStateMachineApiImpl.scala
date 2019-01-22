package de.retest.guistatemachine.api.impl

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi, Id}

class GuiStateMachineApiImpl extends GuiStateMachineApi {
  private var stateMachines = IdMap[GuiStateMachine]()

  override def createStateMachine(): Id = stateMachines.addNewElement(new GuiStateMachineImpl)

  override def removeStateMachine(id: Id): Boolean = stateMachines.removeElement(id)

  override def getStateMachine(id: Id): Option[GuiStateMachine] = stateMachines.getElement(id)

  override def clear(): Unit = stateMachines.clear()

  override def save(filePath: String): Unit = {
    val oos = new ObjectOutputStream(new FileOutputStream(filePath))
    oos.writeObject(stateMachines)
    oos.close()
  }

  override def load(filePath: String): Unit = {
    clear()
    val ois = new ObjectInputStream(new FileInputStream(filePath))
    val readStateMachines = ois.readObject.asInstanceOf[IdMap[GuiStateMachine]]
    ois.close()
    stateMachines = readStateMachines
  }
}
