package com.example

import cats.data.Kleisli
import cats.syntax.option._
import monocle.{Iso, Prism}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try

case class Fahrenheit(value: Double)

case class Celsius(value: Double)

object TemperatureOptics {
  import cats.instances.option._

  val stringToFahrenheit =
    Prism[String, Fahrenheit](input => Try(input.toDouble).toOption.map(d => Fahrenheit(d)))(_.value.toString)

  val stringToCelsius =
    Prism[String, Celsius](input => Try(input.toDouble).toOption.map(d => Celsius(d)))(_.value.toString)

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

  val celsiusToFahrenheit = fahrenheitToCelsius.reverse

  val celsiusStringToFahrenheitString = {
    val celsStringToFahr = Kleisli(stringToCelsius.composeIso(celsiusToFahrenheit).getOption)
    val fahrToString = Kleisli((stringToFahrenheit.reverseGet _).andThen(_.some))
    val composed: Kleisli[Option, String, String] = celsStringToFahr.andThen(fahrToString)

    composed.run
  }

  val fahrenheitStringToCelsiusString = {
    val fahrStringToCels = Kleisli(stringToFahrenheit.composeIso(fahrenheitToCelsius).getOption)
    val celsiusToString = Kleisli((stringToCelsius.reverseGet _).andThen(_.some))
    val composed = fahrStringToCels.andThen(celsiusToString)

    composed.run
  }
}

/**
  * In which composition of different type of Optics is presented.
  */
class TemperatureExample extends WordSpec with Matchers {
  import TemperatureOptics._

  "celsiusStringToFahrenheitString" should {
    "work" in {
      celsiusStringToFahrenheitString("20") should equal ("68.0".some)
      celsiusStringToFahrenheitString("0") should equal ("32.0".some)
      celsiusStringToFahrenheitString("30") should equal ("86.0".some)
      celsiusStringToFahrenheitString("10.5") should equal ("50.9".some)
    }
  }

  "fahrenheitStringToCelsiusString" should {
    "work" in {
      fahrenheitStringToCelsiusString("68") should equal ("20.0".some)
      fahrenheitStringToCelsiusString("32") should equal ("0.0".some)
      fahrenheitStringToCelsiusString("86") should equal ("30.0".some)
      fahrenheitStringToCelsiusString("50.9") should equal ("10.5".some)
    }
  }
}
