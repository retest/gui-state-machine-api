package de.retest.guistatemachine.api.impl.serialization

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineSerializer}

class GuiStateMachineJavaObjectStreamSerializer(guiStateMachine: GuiStateMachine) extends GuiStateMachineSerializer {

  /**
    * Stores the state machine on the disk. Persistence can be useful when the state machines become quite big and the
    * generation/modification is interrupted and continued later.
    *
    * @param filePath The file which the state machine is stored into.
    */
  override def save(filePath: String): Unit = {
    val oos = new ObjectOutputStream(new FileOutputStream(filePath))
    try {
      oos.writeObject(guiStateMachine)
    } finally {
      oos.close()
    }
  }

  /**
    * Clears the state machine and loads it from the disk.
    *
    * @param filePath The file which the state machine is loaded from.
    */
  override def load(filePath: String): Unit = {
    val ois = new ObjectInputStream(new FileInputStream(filePath))
    try {
      val readStateMachine = ois.readObject.asInstanceOf[GuiStateMachineImpl]
      guiStateMachine.assignFrom(readStateMachine)
    } finally {
      ois.close()
    }
  }
}
