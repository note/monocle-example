package com.example.common

import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers
import org.typelevel.discipline.Laws

class OpticsSpec extends FlatSpec with ArbitraryInstances {
  // taken from io.circe.tests.CirceSuite
  def checkLaws(name: String, ruleSet: Laws#RuleSet): Unit =
    ruleSet.all.properties.zipWithIndex.foreach {
      case ((id, prop), 0) => name should s"obey $id" in Checkers.check(prop, Checkers.MinSuccessful(200))
      case ((id, prop), _) => it should s"obey $id" in Checkers.check(prop, Checkers.MinSuccessful(200))
    }
}
