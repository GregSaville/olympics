package org.savvy.olympics.repos.repositories.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.repos.entities.OlympicEvent
import org.savvy.olympics.repos.repositories.EventRepo
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import java.util.*

@Component
class EventRepoImpl(
    private val entityManager: EntityManager
) : EventRepo {
    companion object {
        val LOG = LoggerFactory.getLogger(EventRepoImpl::class.java)
    }

    override fun findById(eventId: UUID): OlympicEvent? {
        LOG.debug("Finding Event with id")
        val eventIdParam = "eventIdParam"

        val queryStr = """
            SELECT oe FROM OlympicEvent oe WHERE oe.id = :$eventIdParam
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, OlympicEvent::class.java)
            .setParameter(eventIdParam, eventId)

        return events(query).first()
    }

    override fun create(olympicEvent: OlympicEvent): OlympicEvent {
        return entityManager.merge(olympicEvent)
    }

    override fun findAll(): List<OlympicEvent> {
        LOG.debug("Finding Events")

        val queryStr = """
            SELECT oe FROM OlympicEvent oe
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, OlympicEvent::class.java)

        return events(query)
    }

    private fun events(query: TypedQuery<OlympicEvent>) = try {
        query.resultList!!
    } catch (e: EmptyResultDataAccessException) {
        UserRepoImpl.LOG.error("Error while querying", e)
        throw NotFound("no found olympians")
    }
}