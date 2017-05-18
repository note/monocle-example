package com.example

import monocle.Prism

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
  val stringToIntPrism = Prism[String, Int](str => Try(str.toInt).toOption)(_.toString)
  val intToPercentPrism = Prism[Int, Percent](i => Percent.fromInt(i))(_.value)
}

object DowncastingPrisms {
  val stringToIntPrism = Prism[String, Int]{ str =>
    println("bazinga: " + str)
    val regex = "(-?[1-9][0-9]*)|0".r
    if(regex.pattern.matcher(str).matches) {
      Try(str.toInt).toOption
    } else {
      None
    }
  }(_.toString)
  val intToPercentPrism = Prism[Int, Percent](i => Percent.fromInt(i))(_.value)
}

object NaivePrismExample {
  import NaivePrisms._

  def main(args: Array[String]): Unit = {
    println("basic operations for inputs parsable to Double")
    println(stringToIntPrism.getOption("22"))
    println(stringToIntPrism.set(40)("22"))
    println(stringToIntPrism.modify(_ + 1)("22"))

    println("basic operations for inputs not parsable to Double")
    println(stringToIntPrism.getOption("someString"))
    println(stringToIntPrism.set(40)("someString"))
    println(stringToIntPrism.modify(_ + 1)("someString"))

    println("basic operations for inputs not parsable to Double - option variants of methods")
    println(stringToIntPrism.setOption(40)("22"))
    println(stringToIntPrism.modifyOption(_ + 1)("22"))

    val stringToPercent = stringToIntPrism.composePrism(intToPercentPrism)
    val testPrism = printOutput(stringToIntPrism.getOption _)_

    testPrism("someString")
    testPrism("22.3")
    testPrism("22.0")
  }

  private def printOutput[T](callback: String => T)(input: String): Unit = {
    val output = callback(input)
    println(s"output for $input: $output")
  }
}
