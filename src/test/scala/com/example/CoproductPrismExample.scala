package com.example

import monocle.Prism
import org.scalatest.{Matchers, WordSpec}

sealed trait Json
case object JNull extends Json
case class JStr(v: String) extends Json
case class JNum(v: Double) extends Json
case class JObj(v: Map[String, Json]) extends Json

class CoproductPrismExample extends WordSpec with Matchers {
  val stringPrism = Prism.partial[Json, String]{case JStr(v) => v}(JStr)

  "stringPrism" should {
    "change me" in {
      stringPrism.getOption(JStr("someString")) should equal(Some("someString"))
      stringPrism.reverseGet("someString") should equal(JStr("someString"))
    }
  }
}
