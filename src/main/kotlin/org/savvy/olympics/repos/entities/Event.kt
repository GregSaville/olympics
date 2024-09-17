package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import java.util.UUID

@Entity
data class Event(
    @Id
    val id: UUID,

    val name: String,

    val location: String,

    val tournamentId: UUID,

    @OneToMany
    val lifeLongParticipants: List<Team>,

    @OneToMany
    val outComes: List<Outcome>,

    @OneToOne
    val activeTeamRed: Team?,

    @OneToOne
    val activeTeamBlue: Team?,
)

