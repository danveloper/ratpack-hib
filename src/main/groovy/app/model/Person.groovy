package app.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Person {
  @Id
  @GeneratedValue
  Long id

  String firstName
  String lastName
}
