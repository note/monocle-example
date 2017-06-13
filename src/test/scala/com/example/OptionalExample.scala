package com.example

import monocle._
import org.scalatest.{FlatSpec, Matchers}

sealed trait OperationError

case class ErrorA(message: String, details: DetailedErrorA) extends OperationError
case object ErrorB extends OperationError

case class DetailedErrorA(detailedMessage: String)

object ErrorOptics {
  // That's straightforward approach, not recommended but shows the essence of Optional:
  val detailedErrorA = Optional[OperationError, String]{
    case err: ErrorA => Some(err.details.detailedMessage)
    case _ => None
  }{ newDetailedMsg => from =>
    from match {
      case err: ErrorA => err.copy(details = err.details.copy(newDetailedMsg))
      case _ => from
    }
  }

  // Better approach is to get `Optional[OperationError, String]` by composing Prism and Lens:
  val errorA = Prism.partial[OperationError, ErrorA] {
    case err: ErrorA => err
  }(identity)

  val detailedError =
    Lens[ErrorA, DetailedErrorA](_.details)(newDetails => from => from.copy(details = newDetails))

  val detailedErrorMsg =
    Lens[DetailedErrorA, String](_.detailedMessage)(newMsg => from => from.copy(detailedMessage = newMsg))

  val composedDetailedErrorA: Optional[OperationError, String] =
    errorA.composeLens(detailedError.composeLens(detailedErrorMsg))
}

class OptionalExample extends FlatSpec with Matchers {
  import ErrorOptics._

  val examples = List(
    Example(ErrorA("msg", DetailedErrorA("detailedMessage")), Some(ErrorA("msg", DetailedErrorA("DETAILEDMESSAGE")))),
    Example(ErrorB, None)
  )

  "detailedErrorA Optional" should "work as expected" in {
    val modify = detailedErrorA.modifyOption(_.toUpperCase)

    testExamples { example =>
      modify(example.input) should equal(example.expectedOutput)
    }(examples)
  }

  "composedDetailedErrorA Optional" should "work as expected" in {
    val modify = detailedErrorA.modifyOption(_.toUpperCase)

    testExamples { example =>
      modify(example.input) should equal(example.expectedOutput)
    }(examples)
  }

  def testExamples(operationToTest: Example => Unit)(examples: Seq[Example]) = {
    examples.foreach { example =>
      operationToTest(example)
    }
  }

  case class Example(input: OperationError, expectedOutput: Option[OperationError])
}
