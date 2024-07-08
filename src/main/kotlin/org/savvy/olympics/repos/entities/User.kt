package org.savvy.olympics.repos.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class User(
    @Id
    val phoneNumber: String,
    var userName :String? = null,
    val registrationDate: LocalDateTime = LocalDateTime.now()
)