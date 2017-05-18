package com.example.laws

import com.example.NaivePrisms
import monocle.law.discipline.PrismTests
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{FlatSpec, WordSpec}
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.typelevel.discipline.Laws

//class StringGen extends Gen[String] {
//
//}

class NaivePrismSpec extends FlatSpec {
  implicit val myArbString: Arbitrary[String] = Arbitrary(Gen.alphaNumStr)

  import scalaz.std.string._
  import scalaz.std.anyVal._
  val stringToDoubleRuleSet = PrismTests(NaivePrisms.stringToDoublePrism)
  val doubleToIntRuleSet = PrismTests(NaivePrisms.doubleToIntPrism)
  checkLaws("stringToDoublePrism", stringToDoubleRuleSet)
  checkLaws("doubleToIntPrism", doubleToIntRuleSet)

  private def checkLaws(name: String, ruleSet: Laws#RuleSet): Unit = ruleSet.all.properties.zipWithIndex.foreach {
    case ((id, prop), 0) => name should s"obey $id" in Checkers.check(prop, Checkers.MinSuccessful(200))
    case ((id, prop), _) => it should s"obey $id" in Checkers.check(prop, Checkers.MinSuccessful(200))
  }
}
