package org.savvy.olympics.domains.services

import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.repos.entities.Tournament
import org.savvy.olympics.repos.repositories.TournamentRepo
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class TournamentService(
    private val tournamentRepo: TournamentRepo,
    private val userService: UserService,
){

    companion object {
        val g = OlympicsLogger
    }

    private var tournamentId : UUID? = null
    fun getTournament(): Tournament? {
        return tournamentId?.let { tournamentRepo.findById(it).getOrNull() }
    }

    fun createTournament(tournamentName: String?): Tournament {
        val newTournament = Tournament()

        val participants = userService.findAllParticipants()

        newTournament.participants = participants
        newTournament.olympicEvents = emptyList()

        g.log.enter(log = Log(
            message = "!!!!!!!++++++++++++===Tournament ${newTournament.name ?: tournamentId} has Begun!===++++++++++++!!!!!!!",
            location = TournamentService::class.java.name
        )
        )

        tournamentRepo.save(newTournament)
        tournamentId = newTournament.id
        return newTournament
    }
}