package org.savvy.olympics.repos.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.util.UUID

@Entity
data class Team(
    @Id
    val id: UUID,

    @OneToOne
    val playerOne: User,

    @OneToOne
    val playerTwo: User?,

    val totalPoints: Long,

    @OneToOne
    val activeEvent: Event,

    @OneToMany(mappedBy = "winner")
    val wins: List<Outcome>,

    @OneToMany(mappedBy = "loser")
    val loses: List<Outcome>
)