package com.example.laws

import com.example.DowncastingPrisms
import monocle.law.discipline.PrismTests
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.anyvals.PosInt
import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers
import org.typelevel.discipline.Laws

class DowncastingPrismSpec extends FlatSpec {
//  val myArbString: Arbitrary[String] = Arbitrary(Gen.alphaNumStr)
  val numbers = ('0' to '9').map(n => Gen.freqTuple(8, n))
  val letters = "@ ,xyz".toCharArray.map(n => Gen.freqTuple(1, n))
  val freqTuples = (numbers ++ letters).toList

  val myArbString = Arbitrary(Gen.frequency(freqTuples:_*))
//  Gen.someOf(Gen.frequency(freqTuples:_*))
  val a: Gen[List[Char]] = Gen.listOf(Gen.frequency(freqTuples:_*))
  val b = Arbitrary(a.map(_.mkString))

  checkPrisms(b, "arbitrary", 200)
//  checkPrisms(Arbitrary(Gen.const("6.0")), "hardcoded", 1)
//  checkPrisms(Arbitrary(Gen.const("006.0")), "hardcoded", 1)

  def checkPrisms(arbString: Arbitrary[String], stringGenName: String, minSuccesses: Int) = {
    import scalaz.std.anyVal._
    import scalaz.std.string._

    implicit val arbStr = arbString
    val stringToDoubleRuleSet = PrismTests(DowncastingPrisms.stringToDoublePrism)
    val doubleToIntRuleSet = PrismTests(DowncastingPrisms.doubleToIntPrism)
    checkLaws("stringToDoublePrism", stringToDoubleRuleSet, stringGenName, minSuccesses)
    checkLaws("doubleToIntPrism", doubleToIntRuleSet, stringGenName, minSuccesses)
  }

  private def checkLaws(name: String, ruleSet: Laws#RuleSet, stringGenName: String, minSuccesses: Int): Unit = ruleSet.all.properties.zipWithIndex.foreach {
    case ((id, prop), 0) => name should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
    case ((id, prop), _) => it should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
  }
}
