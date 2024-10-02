package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Olympian(
    @Id @GeneratedValue
    val id: UUID = UUID.randomUUID(),
    val phoneNumber: String,
    var verifiedOtp: String? = null,
    var userName :String? = null,
    @OneToOne
    @JoinColumn(name = "team_id")
    var team: Team? = null,
    val participating: Boolean = false,
    val registrationDate: LocalDateTime = LocalDateTime.now()
)