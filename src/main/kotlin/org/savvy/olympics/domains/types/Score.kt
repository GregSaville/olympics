package org.savvy.olympics.domains.types

import org.savvy.olympics.domains.types.errors.InvalidCupError

sealed interface Score

class BeerPongScore: Score {
    private var redTeamCups: MutableList<Boolean> = MutableList(10) { true }
    private var blueTeamCups: MutableList<Boolean> = MutableList(10) { true }

    fun hitCup(teamRed: Boolean, cupIndex: Int) {
        if (teamRed) {
            if (cupIndex in redTeamCups.indices) {
                redTeamCups[cupIndex] = false
            } else {
                throw InvalidCupError(cupIndex)            }
        } else {
            if (cupIndex in blueTeamCups.indices) {
                blueTeamCups[cupIndex] = false
            } else {
               throw InvalidCupError(cupIndex)
            }
        }
    }

    fun reverseHit(teamRed: Boolean, cupIndex: Int) {
        if (teamRed) {
            if (cupIndex in redTeamCups.indices) {
                redTeamCups[cupIndex] = true
            } else {
                throw InvalidCupError(cupIndex)            }
        } else {
            if (cupIndex in blueTeamCups.indices) {
                blueTeamCups[cupIndex] = true
            } else {
                throw InvalidCupError(cupIndex)
            }
        }
    }

    fun resetGame() {
        redTeamCups = MutableList(10) { true }
        blueTeamCups = MutableList(10) { true }
    }

    fun checkWinner(): Boolean {
        return when {
            redTeamCups.all { !it } -> true
            blueTeamCups.all { !it } -> true
            else -> false
        }
    }
}
