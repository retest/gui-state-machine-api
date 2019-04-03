package de.retest.guistatemachine.api

import de.retest.surili.commons.actions.NavigateToAction

class HashIdentifierSpec extends AbstractApiSpec {
  private val action0 = new NavigateToAction("http://google.com")
  private val action0Identifier = new HashIdentifier(action0)
  private val action1 = new NavigateToAction("http://wikipedia.org")
  private val action1Identifier = new HashIdentifier(action1)

  "HashIdentifier" should {
    "generate SHA hashes" in {
      action0Identifier.hash shouldEqual "fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f"
      action1Identifier.hash shouldEqual "240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084"
    }

    "not equal" in {
      action0Identifier.equals(action1Identifier) shouldEqual false
      action0Identifier.hashCode() should not equal action1Identifier.hashCode()
    }

    "equal" in {
      action0Identifier.equals(action0Identifier) shouldEqual true
      action0Identifier.hashCode() shouldEqual action0Identifier.hashCode()
    }
  }
}
