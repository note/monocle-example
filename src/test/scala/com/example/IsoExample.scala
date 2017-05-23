package com.example

import monocle._
import org.scalatest.{Matchers, WordSpec}

case class Meter(whole: Int, fraction: Int) {
  override def toString: String = {
    s"$whole.${fraction}m"
  }
}

case class Centimeter(whole: Int) {
  override def toString: String = {
    s"${whole}cm"
  }
}

class IsoExample extends WordSpec with Matchers {
  val centimeterToMeterIso = Iso[Centimeter, Meter] { cm =>
    Meter(cm.whole / 100, cm.whole % 100)
  }{ m =>
    Centimeter(m.whole * 100 + m.fraction)
  }

  val intCentimeter     = Iso[Int, Centimeter](Centimeter.apply)(_.whole)
  val wholeMeterLens    = Lens[Meter, Int](_.whole)(newWhole => prevMeter => prevMeter.copy(whole = newWhole))
  val stringToWholeMeter = DowncastingPrisms.stringToIntPrism.
    composeIso(intCentimeter).
    composeIso(centimeterToMeterIso).
    composeLens(wholeMeterLens)

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
