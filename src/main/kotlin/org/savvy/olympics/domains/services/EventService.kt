package org.savvy.olympics.domains.services

import jakarta.transaction.Transactional
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.LogId
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.domains.types.BeerPong
import org.savvy.olympics.domains.types.Relay
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
    private val teamRepo: TeamRepo,
    private val tournamentRepo: TournamentRepo,
    private val tournamentService: TournamentService
) {

    @Transactional
    fun getEvents(): List<OlympicEvent> {
        return eventRepo.findAll()
    }

    @Transactional
    fun getEvent(id: UUID): OlympicEvent {
        return eventRepo.findById(eventId = id) ?: throw NotFound("No Event with Id $id")
    }

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

        val relay = OlympicEvent(
            name = "RELAY",
            type = Relay,
            location = "TBD",
            tournamentId = currentTournament.id,
            lifeLongParticipants = listOf(),
            outComes = listOf(),
            activeTeamRed = null,
            activeTeamBlue = null,
        )


        eventRepo.create(beerPong)
        eventRepo.create(relay)

        currentTournament.addEvents(listOf(beerPong, relay))

        OlympicsLogger.log.enter(log = Log(
            location = EventService::class.java.name,
            message = "Successfully seeded events for Tournament ${currentTournament.name}",
        ))

        tournamentRepo.save(currentTournament)

        return currentTournament
    }

    @Transactional
    fun joinEvent(redTeamId: UUID?, blueTeamId: UUID?, eventId: UUID): EventResponse {

        val event = eventRepo.findById(eventId) ?: throw NotFound("Event $eventId")

        val redTeam = redTeamId?.let {
            teamRepo.findById(redTeamId) ?: throw NotFound("Red Team $redTeamId for Event $eventId")
        }

        val blueTeam = blueTeamId?.let {
            teamRepo.findById(blueTeamId) ?: throw NotFound("Red Team $blueTeamId for Event $eventId")
        }

        redTeam?.let {
            event.activeTeamRed == redTeam
        }

        blueTeam?.let {
            event.activeTeamBlue == blueTeam
        }

        return EventResponse.Joined(redTeamId, blueTeamId, event.id)
    }

}

sealed interface EventResponse {
    data class Joined(val redTeamId: UUID?, val bluedTeamId: UUID?, val eventId: UUID): EventResponse

    data class Queued(val redTeamId: UUID, val bluedTeamId: UUID, val eventId: UUID): EventResponse

    data class Left(val redTeamId: UUID, val bluedTeamId: UUID, val eventId: UUID): EventResponse
}