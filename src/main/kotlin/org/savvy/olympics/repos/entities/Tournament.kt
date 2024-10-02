package org.savvy.olympics.repos.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class Tournament(
    @Id
    val id: UUID = UUID.randomUUID(),

    var name: String? = null,

    @OneToMany
    var participants: List<Olympian> = mutableListOf(),

    @OneToMany
    var olympicEvents: List<OlympicEvent> = mutableListOf()
)