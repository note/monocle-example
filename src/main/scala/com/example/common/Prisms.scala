package com.example.common

import monocle.Prism

import scala.util.Try

object Prisms {
  val stringToDoublePrism = Prism[String, Double](input => Try(input.toDouble).toOption)(_.toString)
  val doubleToLongPrism    = Prism[Double, Long]{
    case s if s % 1 == 0 => Some(s.toLong)
    case _ => None
  }(_.toDouble)
}
