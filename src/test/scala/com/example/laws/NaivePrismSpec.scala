package com.example.laws

import com.example.NaivePrisms
import com.example.common.OpticsSpec
import monocle.law.discipline.PrismTests

class NaivePrismSpec extends OpticsSpec {
  import scalaz.std.anyVal._
  import scalaz.std.string._

  val stringToIntRuleSet = PrismTests(NaivePrisms.stringToIntPrism)
  val intToPercentRuleSet = PrismTests(NaivePrisms.intToPercentPrism)
  checkLaws("stringToIntPrism", stringToIntRuleSet, 15)
  checkLaws("intToPercentPrism", intToPercentRuleSet)
}
