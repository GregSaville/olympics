package org.savvy.olympics.repos.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.savvy.olympics.domains.types.OlympicEventType
import java.util.UUID

@Entity
data class EventOutcome(
    @Id
    val id: UUID,

    @JoinColumn
    val winner: UUID,

    @JoinColumn
    val loser: UUID,
)