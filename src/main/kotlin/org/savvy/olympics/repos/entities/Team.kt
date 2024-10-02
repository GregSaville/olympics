package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import org.savvy.olympics.api.UserDto
import org.savvy.olympics.api.toDto
import java.util.UUID

@Entity
data class Team(
    @Id
    val id: UUID = UUID.randomUUID(),

    @OneToOne(mappedBy = "team")
    @JoinColumn(name = "player_one_id")
    var playerOne: Olympian,

    @OneToOne(mappedBy = "team")
    @JoinColumn(name = "player_two_id")
    var playerTwo: Olympian?,

    val totalPoints: Long = 0,

    @OneToOne
    @JoinColumn(name = "active_event_id")
    val activeOlympicEvent: OlympicEvent? = null,

    @OneToMany(mappedBy = "winner")
    val wins: List<EventOutcome> = emptyList(),

    @OneToMany(mappedBy = "loser")
    val loses: List<EventOutcome> = emptyList()
)

fun Team.toUserDtos() = listOfNotNull(
    playerOne.toDto(),
    playerTwo?.toDto()
)