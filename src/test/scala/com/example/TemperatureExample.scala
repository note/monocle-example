package com.example

import monocle.{Fold, Iso, PPrism, Prism}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try

case class Fahrenheit(value: Double)

case class Celsius(value: Double)

object TemperatureOptics {
  val stringToFahrenheit =
    Prism[String, Fahrenheit](input => Try(input.toDouble).toOption.map(d => Fahrenheit(d)))(_.toString)

  val stringToCelsius =
    Prism[String, Celsius](input => Try(input.toDouble).toOption.map(d => Celsius(d)))(_.toString)

  val fahrenheitToCelsius =
    Iso[Fahrenheit, Celsius]{ fahrenheit =>
      val ratio: Double = 5.0 / 9.0
      val celsiusValue = (fahrenheit.value - 32) * ratio
      Celsius(celsiusValue)
    }{ celsius =>
      val ratio: Double = 9.0 / 5.0
      val fahrenheitValue = celsius.value * ratio + 32
      Fahrenheit(fahrenheitValue)
    }
}

/**
  * In which composition of different type of Optics is presented.
  */
class TemperatureExample extends WordSpec with Matchers {
  import TemperatureOptics._

  "composition" should {
    "produce Celsius to Fahrenheit Prism" in {
      // TODO: rewrite with Kleisli
      val a = stringToCelsius.composeIso(fahrenheitToCelsius.reverse).getOption _
      val b: (String) => Option[String] = a andThen (_.map(stringToFahrenheit.reverseGet))
    }

    "produce Celsius to Fahrenheit Prism" in {
      stringToCelsius.composeIso(fahrenheitToCelsius.reverse)
    }
  }
}
