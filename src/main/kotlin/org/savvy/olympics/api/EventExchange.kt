package org.savvy.olympics.api

import org.savvy.olympics.domains.services.EventResponse
import org.savvy.olympics.domains.services.EventService
import org.savvy.olympics.repos.entities.OlympicEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange


@HttpExchange("/event", accept = [MediaType.APPLICATION_JSON_VALUE])
interface EventExchange {

    @GetMapping
    fun getEvents(): ResponseEntity<List<EventDto>>

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: UUID): ResponseEntity<OlympicEvent>

    @PostMapping
    fun joinEvent(@RequestBody body: JoinEventRequest): ResponseEntity<EventResponse>
}

@RestController
class EventController(
    private val eventService: EventService
): EventExchange {
    override fun getEvents(): ResponseEntity<List<EventDto>> {
        return ResponseEntity.ok(eventService.getEvents().toDtos())
    }

    override fun getEvent(id: UUID): ResponseEntity<OlympicEvent> {
        return ResponseEntity.ok(eventService.getEvent(id))
    }

    override fun joinEvent(body: JoinEventRequest): ResponseEntity<EventResponse> {
        return with(body) {
            ResponseEntity.ok(eventService.joinEvent(redTeamId, blueTeamId, eventId))
        }
    }
}

data class JoinEventRequest(
    val eventId: UUID,
    val redTeamId: UUID?,
    val blueTeamId: UUID?
)

data class EventDto(
    val id: UUID,
    val name: String
)

fun List<OlympicEvent>.toDtos() = this.map { it.toDto() }

fun OlympicEvent.toDto() = EventDto(
    id = this.id,
    name = this.name
)