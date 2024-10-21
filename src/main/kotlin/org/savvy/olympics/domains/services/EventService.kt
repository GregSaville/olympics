package org.savvy.olympics.domains.services

import jakarta.transaction.Transactional
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.LogId
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.domains.types.BeerPong
import org.savvy.olympics.repos.entities.OlympicEvent
import org.savvy.olympics.repos.entities.Tournament
import org.savvy.olympics.repos.repositories.EventRepo
import org.savvy.olympics.repos.repositories.TeamRepo
import org.savvy.olympics.repos.repositories.TournamentRepo
import org.savvy.olympics.repos.repositories.UserRepo
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.UUID

@Component
class EventService(
    private val eventRepo: EventRepo,
    private val tournamentRepo: TournamentRepo,
    private val tournamentService: TournamentService
) {

    @Transactional
    fun seedEvents(): Tournament {

        val currentTournament = tournamentService.getTournament() ?: throw RuntimeException("No current tournament")

        val beerPong = OlympicEvent(
            name = "BEER PONG",
            type = BeerPong,
            location = "TBD",
            tournamentId = currentTournament.id,
            lifeLongParticipants = listOf(),
            outComes = listOf(),
            activeTeamRed = null,
            activeTeamBlue = null
        )


        eventRepo.create(beerPong)

        currentTournament.addEvents(listOf(beerPong))

        OlympicsLogger.log.enter(log = Log(
            location = EventService::class.java.name,
            message = "Successfully seeded events for Tournament ${currentTournament.name}",
        ))

        tournamentRepo.save(currentTournament)

        return currentTournament
    }

    fun joinEvent(redTeamId: UUID?, blueTeamId: UUID?, eventId: UUID,) {

    }
}