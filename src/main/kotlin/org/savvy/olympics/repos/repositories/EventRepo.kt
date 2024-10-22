package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.OlympicEvent
import java.util.UUID

interface EventRepo {

    fun findById(eventId: UUID): OlympicEvent?

    fun create(olympicEvent: OlympicEvent): OlympicEvent

    fun findAll(): List<OlympicEvent>
}