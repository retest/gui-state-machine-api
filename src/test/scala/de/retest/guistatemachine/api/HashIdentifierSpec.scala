package de.retest.guistatemachine.api

class HashIdentifierSpec extends AbstractApiSpec {

  "HashIdentifier" should {
    "generate SHA hashes" in {
      action0Identifier.hash shouldEqual "fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f"
      action1Identifier.hash shouldEqual "240d08498736de4d893c146fd64b58b1ae1eda8c36a565919b035d86c6ee2084"
    }

    "not equal" in {
      action0Identifier.equals(action1Identifier) shouldEqual false
      action0Identifier.hashCode() should not equal action1Identifier.hashCode()
      action0Identifier.equals(10) shouldEqual false
    }

    "equal" in {
      action0Identifier.equals(action0Identifier) shouldEqual true
      action0Identifier.hashCode() shouldEqual action0Identifier.hashCode()
    }

    "be converted into a string" in {
      action0Identifier.toString shouldEqual "ActionIdentifier[action=NavigateToAction(url=http://google.com), hash=fd00ea22cb50efd96c3ff59d8900685d0d64f2cee1e77873133e7e186afd2e7f]"
    }
  }
}
