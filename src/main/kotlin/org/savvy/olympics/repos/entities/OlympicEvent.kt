package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import org.savvy.olympics.domains.types.OlympicEvent
import java.util.UUID
import kotlin.jvm.Transient

@Entity
data class OlympicEvent(
    @Id
    val id: UUID = UUID.randomUUID(),

    val name: String,

    @Transient
    val type: OlympicEvent,

    val location: String,

//    var queue: List<Team>,

    val tournamentId: UUID,

    @OneToMany
    var lifeLongParticipants: List<Team>,

    @OneToMany
    var outComes: List<EventOutcome>,

    @OneToOne
    var activeTeamRed: Team?,

    @OneToOne
    var activeTeamBlue: Team?,
)