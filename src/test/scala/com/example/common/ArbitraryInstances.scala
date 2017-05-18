package com.example.common

import com.example.Percent
import org.scalacheck.{Arbitrary, Cogen, Gen}

trait ArbitraryInstances {
  val numbers = ('0' to '9').map(n => Gen.freqTuple(8, n))
  val letters = "@ !.xyz".toCharArray.map(n => Gen.freqTuple(1, n))
  val freqTuples = (numbers ++ letters).toList

  val arbListChars = Gen.listOf(Gen.frequency(freqTuples:_*))
  implicit val arbString = Arbitrary(arbListChars.map(_.mkString))

  implicit val arbPercent = Arbitrary(Gen.chooseNum(0, 100).map(n => Percent(n)))
  implicit val arbPercentF = {
    implicit val percentCogen: Cogen[Percent] = implicitly[Cogen[Int]].contramap[Percent](_.value)
    Arbitrary.arbFunction1[Percent, Percent]
  }
}
