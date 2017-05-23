package com.example.laws.common

import org.scalactic.anyvals.PosZInt
import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers
import org.typelevel.discipline.Laws

class OpticsSpec extends FlatSpec with ArbitraryInstances {
  // taken from io.circe.tests.CirceSuite
  def checkLaws(name: String, ruleSet: Laws#RuleSet, maxSize: Int = 100): Unit = {
    val configParams = List(Checkers.MinSuccessful(200), Checkers.SizeRange(PosZInt.from(maxSize).get))

    ruleSet.all.properties.zipWithIndex.foreach {
      case ((id, prop), 0) => name should s"obey $id" in Checkers.check(prop, configParams:_*)
      case ((id, prop), _) => it should s"obey $id" in Checkers.check(prop, configParams:_*)
    }
  }
}
