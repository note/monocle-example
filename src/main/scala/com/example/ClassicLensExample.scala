package com.example

import com.example.common.{Address, Person, Street}
import monocle.Lens

object ClassicLensExample {
  // to have access to lenses
  import com.example.common.PersonLenses._

  def main(args: Array[String]): Unit = {
   val bob = Person("Bob Dylan", Address("New York", Street("some", 67)))

   println("With copy:" + upperCaseWithCopy(bob))
   println("With lens:" + upperCaseWithLens(bob))
  }

  def upperCaseWithCopy(person: Person): Person =
   person.copy(address = person.address.copy(
     street = person.address.street.copy(
       name = person.address.street.name.toUpperCase
     )
   ))

  def upperCaseWithLens(person: Person): Person = {
    val streetName: Lens[Person, String] = addressLens.composeLens(streetLens).composeLens(nameLens)
    streetName.modify(_.toUpperCase)(person)
  }
}
