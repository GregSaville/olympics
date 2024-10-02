package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import org.savvy.olympics.domains.types.OlympicEventType
import java.util.UUID

@Entity
data class OlympicEvent(
    @Id
    val id: UUID,

    val name: String,

    val type: OlympicEventType,

    val location: String,

    val tournamentId: UUID,

    @OneToMany
    val lifeLongParticipants: List<Team>,

    @OneToMany
    val outComes: List<EventOutcome>,

    @OneToOne
    val activeTeamRed: Team?,

    @OneToOne
    val activeTeamBlue: Team?,
)

