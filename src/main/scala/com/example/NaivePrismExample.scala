package com.example

import monocle.Prism

import scala.util.Try

object NaivePrisms {
  val stringToDoublePrism = Prism[String, Double](input => Try(input.toDouble).toOption)(_.toString)
  val doubleToLongPrism    = Prism[Double, Long]{
    case s if s % 1 == 0 => Some(s.toLong)
    case _ => None
  }(_.toDouble)
}

object NaivePrismExample {
  import NaivePrisms._

  def main(args: Array[String]): Unit = {
    println("basic operations for inputs parsable to Double")
    println(stringToDoublePrism.getOption("22.0"))
    println(stringToDoublePrism.set(40.0)("22.0"))
    println(stringToDoublePrism.modify(_ + 1.0)("22.0"))

    println("basic operations for inputs not parsable to Double")
    println(stringToDoublePrism.getOption("someString"))
    println(stringToDoublePrism.set(40.0)("someString"))
    println(stringToDoublePrism.modify(_ + 1.0)("someString"))

    println("basic operations for inputs not parsable to Double - option variants of methods")
    println(stringToDoublePrism.setOption(40.0)("22.1"))
    println(stringToDoublePrism.modifyOption(_ + 1.0)("22.1"))

    val stringToLongPrism: Prism[String, Long] = stringToDoublePrism.composePrism(doubleToLongPrism)
    val testPrism = printOutput(stringToLongPrism.getOption _)_

//    testPrism("someString")
//    testPrism("22.3")
//    testPrism("22.0")
  }

  private def printOutput[T](callback: String => T)(input: String): Unit = {
    val output = callback(input)
    println(s"output for $input: $output")
  }
}
