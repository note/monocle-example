package com.example.laws

import com.example.NaivePrisms
import com.example.laws.common.OpticsSpec
import monocle.law.discipline.PrismTests

class NaivePrismSpec extends OpticsSpec {
  import scalaz.std.anyVal._
  import scalaz.std.string._

  val stringToIntRuleSet = PrismTests(NaivePrisms.stringToIntPrism)
  val intToPercentRuleSet = PrismTests(NaivePrisms.intToPercentPrism)

  // Since those are NaivePrisms they're not lawful and would fail the tests
  // uncomment following lines to see them failing:
//  checkLaws("stringToIntPrism", stringToIntRuleSet, 15)
//  checkLaws("intToPercentPrism", intToPercentRuleSet)
}
