package com.example.laws

import com.example.NaivePrisms
import monocle.law.discipline.PrismTests
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.anyvals.PosInt
import org.scalatest.{FlatSpec, WordSpec}
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.typelevel.discipline.Laws

//class StringGen extends Gen[String] {
//
//}

class NaivePrismSpec extends FlatSpec {
  val myArbString: Arbitrary[String] = Arbitrary(Gen.alphaNumStr)

  checkPrisms(myArbString, "arbitrary", 200)
  checkPrisms(Arbitrary(Gen.const("006.0")), "hardcoded", 1)

  def checkPrisms(arbString: Arbitrary[String], stringGenName: String, minSuccesses: Int) = {
    import scalaz.std.string._
    import scalaz.std.anyVal._

    implicit val arbStr = arbString
    val stringToDoubleRuleSet = PrismTests(NaivePrisms.stringToDoublePrism)
    val doubleToIntRuleSet = PrismTests(NaivePrisms.doubleToIntPrism)
    checkLaws("stringToDoublePrism", stringToDoubleRuleSet, stringGenName, minSuccesses)
    checkLaws("doubleToIntPrism", doubleToIntRuleSet, stringGenName, minSuccesses)
  }

  private def checkLaws(name: String, ruleSet: Laws#RuleSet, stringGenName: String, minSuccesses: Int): Unit = ruleSet.all.properties.zipWithIndex.foreach {
    case ((id, prop), 0) => name should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
    case ((id, prop), _) => it should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
  }
}
