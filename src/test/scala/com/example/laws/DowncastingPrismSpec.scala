package com.example.laws

import com.example.{DowncastingPrisms, Percent}
import monocle.law.discipline.PrismTests
import org.scalacheck.{Arbitrary, Cogen, Gen}
import org.scalactic.anyvals.PosInt
import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers
import org.typelevel.discipline.Laws
import com.example.Percent._

class DowncastingPrismSpec extends FlatSpec {
  val numbers = ('0' to '9').map(n => Gen.freqTuple(8, n))
  val letters = "@ !.xyz".toCharArray.map(n => Gen.freqTuple(1, n))
  val freqTuples = (numbers ++ letters).toList

  val arbListChars = Gen.listOf(Gen.frequency(freqTuples:_*))
  val arbString = Arbitrary(arbListChars.map(_.mkString))

  // TODO: Extract
  implicit val arbPercent = Arbitrary(Gen.chooseNum(0, 100).map(n => Percent(n)))
  implicit val percentCogen: Cogen[Percent] = implicitly[Cogen[Int]].contramap[Percent](_.value)
  implicit val arbPercentF = Arbitrary.arbFunction1[Percent, Percent]

  checkPrisms(arbString, "arbitrary", 200)
//  checkPrisms(Arbitrary(Gen.const("007")), "hardcoded", 1)

  def checkPrisms(arbString: Arbitrary[String], stringGenName: String, minSuccesses: Int) = {
    import scalaz.std.anyVal._
    import scalaz.std.string._

    implicit val arbStr = arbString
    val stringToDoubleRuleSet = PrismTests(DowncastingPrisms.stringToIntPrism)
    val doubleToIntRuleSet = PrismTests(DowncastingPrisms.intToPercentPrism)
    checkLaws("stringToDoublePrism", stringToDoubleRuleSet, stringGenName, minSuccesses)
    checkLaws("doubleToIntPrism", doubleToIntRuleSet, stringGenName, minSuccesses)
  }

  // taken from io.circe.tests.CirceSuite
  private def checkLaws(name: String, ruleSet: Laws#RuleSet, stringGenName: String, minSuccesses: Int): Unit = ruleSet.all.properties.zipWithIndex.foreach {
    case ((id, prop), 0) => name should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
    case ((id, prop), _) => it should s"obey $id $stringGenName" in Checkers.check(prop, Checkers.MinSuccessful(PosInt.from(minSuccesses).get))
  }
}
