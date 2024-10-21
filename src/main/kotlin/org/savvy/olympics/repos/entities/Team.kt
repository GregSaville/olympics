package org.savvy.olympics.repos.entities

import jakarta.persistence.*
import org.savvy.olympics.api.UserDto
import org.savvy.olympics.api.toDto
import java.util.UUID
import kotlin.random.Random

@Entity
data class Team(
    @Id
    val id: UUID = UUID.randomUUID(),

    var name: String = generateTeamName(),

    var color: String = generateTeamColor(),

    @OneToOne
    @JoinColumn(name = "player_one_id")
    var playerOne: Olympian,

    @OneToOne
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

fun generateTeamColor(): String {
    val chars = "0123456789ABCDEF"
    var color = "#"
    for (i in 0..5) {
        color += chars[Random.nextInt(16)]
    }
    return color
}

fun generateTeamName(): String {
    val alcoholTerms = listOf("Whiskey River", "Vodka", "Rum", "Tequila", "Gin", "Bourbon", "Beer", "Black Lager", "French Ale", "Mead")
    val stonerTerms = listOf("Green", "Kush", "Weed", "Joint", "Leaf", "420", "Sativa", "Indica", "Northern Lights", "Scottish Donkey")

    val sillyPrefixes = listOf("Stoner", "Blazed", "Tipsy", "Drunk", "Lit", "High", "Boozy", "Buzzed", "Mighty","Spooky","Smelly","Funky")
    val sillySuffixes = listOf("Gang", "Squad", "Crew", "Party", "Kings", "Buddies", "Team", "Legends")

    val mainTerm = if (Random.nextBoolean()) alcoholTerms.random() else stonerTerms.random()

    val prefix = sillyPrefixes.random()
    val suffix = sillySuffixes.random()

    return "$prefix $mainTerm $suffix"
}