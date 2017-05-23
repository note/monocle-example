package com.example

import monocle.Prism
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try
import scalaz.Equal

case class Percent private (value: Int) {
  require(value >= 0)
  require(value <= 100)
}

object Percent {
  def fromInt(input: Int): Option[Percent] =
    if(input >= 0 && input <= 100) {
      Some(Percent(input))
    } else {
      None
    }

  implicit val equalInstance: Equal[Percent] = (p1: Percent, p2: Percent) => p1 == p2
}

object NaivePrisms {
  // this is not a lawful Prism but in naive version we don't care about it
  // for lawful Prism take a look at DowncastingPrisms
  val stringToIntPrism = Prism[String, Int](str => Try(str.toInt).toOption)(_.toString)
  val intToPercentPrism = Prism[Int, Percent](i => Percent.fromInt(i))(_.value)
}

object DowncastingPrisms {
  val regex = "(-?[1-9][0-9]*)|0".r

  val stringToIntPrism = Prism[String, Int]{ str =>
    if(regex.pattern.matcher(str).matches) {
      Try(str.toInt).toOption
    } else {
      None
    }
  }(_.toString)
  val intToPercentPrism = Prism[Int, Percent](i => Percent.fromInt(i))(_.value)
}

object NaivePrismExample extends WordSpec with Matchers {
  import NaivePrisms._

  "stringToIntPrism" should {
    "work for inputs parsable to Double" in {
      stringToIntPrism.getOption("22") should equal(Some(22))
      stringToIntPrism.set(40)("22") should equal("40")
      stringToIntPrism.modify(_ + 1)("22") should equal("23")
    }

    "return None when `getOption` called on unparsable input" in {
      stringToIntPrism.getOption("someString") should equal(None)
    }

    "return unmodified input when `set` called on unparsable input" in {
      stringToIntPrism.set(40)("someString") should equal("someString")
    }

    "return unmodified input when `modify` called on unparsable input" in {
      stringToIntPrism.modify(_ + 1)("someString") should equal("someString")
    }

    "return Option informing about success of `setOption`" in {
      stringToIntPrism.setOption(40)("22") should equal(Some("40"))
      stringToIntPrism.setOption(40)("someString") should equal(None)
    }

    "return Option informing about success of `modifyOption`" in {
      stringToIntPrism.modifyOption(_ + 1)("22") should equal(Some("23"))
      stringToIntPrism.modifyOption(_ + 1)("someString") should equal(None)
    }
  }

  "prism composition" should {
    "work as expected" in {
      val stringToPercent = stringToIntPrism.composePrism(intToPercentPrism)
      stringToPercent.getOption("someString") should equal(None)
      stringToPercent.getOption("188") should equal(None)
      stringToPercent.getOption("88.0") should equal(None)
      stringToPercent.getOption("88") should equal(Some(Percent(80)))
    }
  }

}
