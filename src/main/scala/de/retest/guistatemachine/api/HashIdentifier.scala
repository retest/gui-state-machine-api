package de.retest.guistatemachine.api

import java.io.{ByteArrayOutputStream, ObjectOutputStream, Serializable}

/**
  * Storing the whole states and actions as XML takes too much space. Therefore, we only store a hash value. The hash
  * should have no collisions to uniquely identify states and actions. Since we use SHA-256 the probability is very low
  * that any collisions will occur.
  *
  * @param serializable The serializable from which a SHA-256 is generated.
  */
@SerialVersionUID(1L)
class HashIdentifier(serializable: Serializable) extends scala.Serializable {
  val hash: String = HashIdentifier.sha256Hash(HashIdentifier.serializableToString(serializable))

  override def equals(obj: Any): Boolean =
    if (!obj.isInstanceOf[HashIdentifier]) {
      false
    } else {
      val other = obj.asInstanceOf[HashIdentifier]
      hash.equals(other.hash)
    }

  override def hashCode(): Int = hash.hashCode

  override def toString: String = s"hash=$hash"

}

object HashIdentifier {
  def sha256Hash(text: String): String =
    String.format("%064x", new java.math.BigInteger(1, java.security.MessageDigest.getInstance("SHA-256").digest(text.getBytes("UTF-8"))))

  def serializableToString(serializable: Serializable): String = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(byteArrayOutputStream)
    try {
      oos.writeObject(serializable)
      byteArrayOutputStream.toString("UTF-8")
    } finally {
      oos.close()
    }
  }
}
