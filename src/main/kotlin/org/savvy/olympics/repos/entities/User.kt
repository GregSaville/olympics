package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue
    val id: UUID = UUID.randomUUID(),
    val phoneNumber: String,
    var userName :String? = null,
    @OneToOne
    var plusOne: User? = null,
    @OneToOne
    var team: Team? = null,
    val participating: Boolean = false,
    val registrationDate: LocalDateTime = LocalDateTime.now()
) {
    fun assignPlusOne(plusOne: User) {
        this.plusOne = plusOne
        plusOne.plusOne = this
    }
}