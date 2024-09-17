package org.savvy.olympics.repos.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.savvy.olympics.domains.types.EventType
import java.util.UUID

@Entity
data class Outcome(
    @Id
    val id: UUID,

    @ManyToOne
    val winner: Team,

    @ManyToOne
    val loser: Team,

    val eventType: EventType
)