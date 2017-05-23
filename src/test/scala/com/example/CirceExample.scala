package com.example

import io.circe.optics.JsonPath
import org.scalatest.{Matchers, WordSpec}
import io.circe.optics.JsonPath._

class CirceExample extends WordSpec with Matchers {
  "JsonPath" should {
    "should work as non-optics equivalent" in {
      val input = referenceJson("abc")
      val withOptics = modifyWithOptics(input)
      val withoutOptics = modifyWithoutOptics(input)

      withOptics should equal(withoutOptics)
      withOptics should equal(referenceJson("ABC"))
    }
  }

  // let's capitalize street name without optics
  def modifyWithoutOptics(json: io.circe.Json): io.circe.Json =
    json.hcursor.
      downField("order").
      downField("address").
      downField("street").
      withFocus(_.mapString(_.toUpperCase)).top.get

  // now with optics:
  def modifyWithOptics(json: io.circe.Json): io.circe.Json =
    root.order.address.street.string.modify(_.toUpperCase)(json)

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
