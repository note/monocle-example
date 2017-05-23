package com.example.laws

import com.example.{Duration, DurationOptics}
import com.example.laws.common.OpticsSpec
import monocle.law.discipline.LensTests
import org.scalacheck.{Arbitrary, Gen}

/**
  * IMPORTANT: This spec crucial part is actually commented out
  *
  * This code remains in repo to have read-to-run code with which you can check
  * why DurationOptics is not lawful
  */
class DurationOpticsSpec extends OpticsSpec {
  import scalaz.std.anyVal._
  import Duration._

  val durationGen = for {
    hours   <- Gen.chooseNum(0, 100)
    minutes <- Gen.chooseNum(0, 59)
    seconds <- Gen.chooseNum(0, 59)
  } yield Duration(hours, minutes, seconds)
  implicit val arbDuration = Arbitrary(durationGen)

  implicit val arbInt = Arbitrary(Gen.chooseNum(0, 120))

  val hoursRules = LensTests(DurationOptics.hoursL)
  val minutesRules = LensTests(DurationOptics.minutesL)
  val secondsRules = LensTests(DurationOptics.secondsL)

//  checkLaws("hours Lens", hoursRules, maxSize = 3000)
//  checkLaws("minutes Lens", minutesRules, maxSize = 120)
//  checkLaws("seconds Lens", secondsRules, maxSize = 3000)
}
