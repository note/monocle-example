package com.example

import monocle._
import org.scalatest.{Matchers, WordSpec}

// business entities
case class Meter(whole: Int, fraction: Int)
case class Centimeter(whole: Int)

// Optics
object PhysicalUnitsOptics {
  val centimeterToMeterIso = Iso[Centimeter, Meter] { cm =>
    Meter(cm.whole / 100, cm.whole % 100)
  }{ m =>
    Centimeter(m.whole * 100 + m.fraction)
  }

  val intCentimeter     = Iso[Int, Centimeter](Centimeter.apply)(_.whole)
  val wholeMeterLens    = Lens[Meter, Int](_.whole)(newWhole => prevMeter => prevMeter.copy(whole = newWhole))
  val stringToWholeMeter: Optional[String, Int] = DowncastingPrisms.stringToIntPrism.
    composeIso(intCentimeter).
    composeIso(centimeterToMeterIso).
    composeLens(wholeMeterLens)
}

class IsoExample extends WordSpec with Matchers {
  import PhysicalUnitsOptics._

  "centimeterToMeterIso" should {
    "work" in {
      centimeterToMeterIso.modify(m => m.copy(m.whole + 3))(Centimeter(155)) should equal(Centimeter(455))
      centimeterToMeterIso.modify(meter => meter.copy(meter.whole + 3))(Centimeter(155)).toString
    }

    "be more readable with composed Optics" in {
      stringToWholeMeter.modify(_ + 3)("155") should equal("455")
    }
  }
}
