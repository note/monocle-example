package com.example.common

import monocle.Lens

case class Person(fullName: String, address: Address)
case class Address(city: String, street: Street)
case class Street(name: String, number: Int)

object PersonLenses {
  val addressLens = Lens.apply[Person, Address](person => person.address)(newAddress => person => person.copy(address = newAddress))
  val streetLens = Lens.apply[Address, Street](address => address.street)(newStreet => address => address.copy(street = newStreet))
  val nameLens = Lens.apply[Street, String](street => street.name)(newName => street => street.copy(name = newName))
}
