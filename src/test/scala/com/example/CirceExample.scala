package com.example

import io.circe.optics.JsonPath
import org.scalatest.{Matchers, WordSpec}
import io.circe.optics.JsonPath._

class CirceExample extends WordSpec with Matchers {
  val street: JsonPath = root.order.address.street

  "JsonPath" should {
    "work" in new Context {
      val modified = street.string.modify(_.toUpperCase)(referenceJson("abc"))
      modified should equal(referenceJson("ABC"))
    }
  }
}

trait Context {
  def referenceJson(streetName: String) =
    io.circe.parser.parse(s"""
      |{
      |  "order": {
      |    "address": {
      |      "street": "$streetName",
      |      "city": "someCity"
      |    },
      |    "items": [
      |      {
      |        "name": "OK Computer",
      |        "amount": 1
      |      },
      |      {
      |        "name": "Kid A",
      |        "amount": 3
      |      }
      |    ]
      |  }
      |}
    """.stripMargin).right.get
}
