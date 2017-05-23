package com.example

import monocle.Prism
import org.scalatest.{Matchers, WordSpec}

/**
  * Prism for Coproduct - in that case modelled with sealed trait hierarchy.
  *
  * Example idea taken from http://julien-truffaut.github.io/Monocle/optics/prism.html
  */

sealed trait Json
case object JNull extends Json
case class JStr(v: String) extends Json
case class JNum(v: Double) extends Json
case class JObj(v: Map[String, Json]) extends Json

class CoproductPrismExample extends WordSpec with Matchers {
  val stringPrism = Prism.partial[Json, String]{case JStr(v) => v}(JStr)

  "stringPrism" should {
    "work for primitive operations" in {
      stringPrism.getOption(JStr("someString")) should equal(Some("someString"))
      stringPrism.reverseGet("someString") should equal(JStr("someString"))

      // prism `getOption` returns None if does not succeed
      stringPrism.getOption(JNull) should equal(None)
    }

    "work for modify" in {
      val someJson: Json = JStr("someString")

      // first let's try to capitalize JStr with pattern match
      val withPatternMatch = someJson match {
        case JStr(s)      => JStr(s.toUpperCase)
        case anythingElse => anythingElse
      }

      // now the same thing with Prism
      val withPrism = stringPrism.modify(_.toUpperCase)(someJson)

      withPrism should equal(withPatternMatch)
    }
  }
}
