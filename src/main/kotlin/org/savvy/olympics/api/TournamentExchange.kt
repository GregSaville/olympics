package org.savvy.olympics.api

import org.savvy.olympics.domains.services.TournamentService
import org.savvy.olympics.domains.services.UserService
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.domains.services.EventService
import org.savvy.olympics.repos.entities.Tournament
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("/tournament", accept = [MediaType.APPLICATION_JSON_VALUE])
interface TournamentExchange {
    @GetMapping
    fun getTournament(): ResponseEntity<Tournament>

    @PostMapping
    fun createTournament(
        @RequestBody body: CreateTournamentRequest
    ): ResponseEntity<Tournament>

    @PostMapping("/seed-events")
    fun seedEvents(): ResponseEntity<Tournament>
}

@Controller
class TournamentController : TournamentExchange {

    @Autowired
    lateinit var tournamentService: TournamentService

    @Autowired
    lateinit var eventService: EventService

    override fun getTournament(): ResponseEntity<Tournament> {
        return tournamentService.getTournament()?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    override fun createTournament(body: CreateTournamentRequest): ResponseEntity<Tournament> {
        return tournamentService.createTournament(body.name).let {
            ResponseEntity.ok(it)
        } ?:  run {
            OlympicsLogger.log.enter(log = Log(location = TournamentController::class.java.name, message = "Error Creating Tournament ${body.name} See Logs"))
            ResponseEntity.unprocessableEntity().build()
        }
    }

    override fun seedEvents(): ResponseEntity<Tournament> {
        return ResponseEntity.ok(eventService.seedEvents())
    }
}

data class CreateTournamentRequest(
    val name: String?
)