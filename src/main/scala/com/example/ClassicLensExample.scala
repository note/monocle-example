package com.example

import monocle.Lens

case class Person(fullName: String, address: Address)
case class Address(city: String, street: Street)
case class Street(name: String, number: Int)

object ClassicLensExample {
  val addressLens = Lens.apply[Person, Address](person => person.address)(newAddress => person => person.copy(address = newAddress))
  val streetLens = Lens.apply[Address, Street](address => address.street)(newStreet => address => address.copy(street = newStreet))
  val nameLens = Lens.apply[Street, String](street => street.name)(newName => street => street.copy(name = newName))

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
