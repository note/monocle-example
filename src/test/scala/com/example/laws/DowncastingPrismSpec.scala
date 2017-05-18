package com.example.laws

import com.example.DowncastingPrisms
import com.example.Percent._
import com.example.common.OpticsSpec
import monocle.law.discipline.PrismTests

class DowncastingPrismSpec extends OpticsSpec {
  import scalaz.std.anyVal._
  import scalaz.std.string._

  val stringToIntRuleSet = PrismTests(DowncastingPrisms.stringToIntPrism)
  val intToPercentRuleSet = PrismTests(DowncastingPrisms.intToPercentPrism)
  checkLaws("stringToIntPrism", stringToIntRuleSet)
  checkLaws("intToPercentPrism", intToPercentRuleSet)
}
