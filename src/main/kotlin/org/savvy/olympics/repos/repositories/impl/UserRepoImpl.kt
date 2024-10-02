package org.savvy.olympics.repos.repositories.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.repos.entities.Olympian
import org.savvy.olympics.repos.entities.constants.Tables
import org.savvy.olympics.repos.repositories.UserRepo
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepoImpl(val entityManager: EntityManager) : UserRepo {

    companion object {
        val LOG = LoggerFactory.getLogger(UserRepoImpl::class.java)
    }

    override fun findByPhoneNumber(phoneNumber: String): Olympian? {
        LOG.debug("Searching For User with number: $phoneNumber")
        val phoneParam = "phoneParam"

        val queryStr = """
            SELECT o FROM Olympian o where phoneNumber = :$phoneParam
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Olympian::class.java)
            .setParameter(phoneParam, phoneNumber)

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            LOG.warn("No Olympian found with phone number: $phoneNumber")
            null
        }
    }

    override fun findByIds(userIds: List<UUID>): List<Olympian> {
        LOG.debug("Finding Users with ids!")
        val userIdsParam = "userIdsParam"

        val queryStr = """
            SELECT o FROM Olympian o WHERE o.id IN :$userIdsParam
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Olympian::class.java)
            .setParameter(userIdsParam, userIds)

        return olympians(query)
    }

    override fun findParticipants(): List<Olympian> {
        LOG.debug("Finding Participants!")

        val queryStr = """
            SELECT o FROM ${Tables.USER} o where participating = true
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Olympian::class.java)

        return olympians(query)
    }

    override fun save(olympian: Olympian): Olympian {
        LOG.debug("Saving Olympian ${olympian.id}: ${olympian.userName}")
        return entityManager.merge(olympian)
    }

    override fun findAllUsers(): List<Olympian> {
        LOG.debug("Finding All Users")
        val queryStr = """
            SELECT o FROM Olympian o 
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Olympian::class.java)

        return olympians(query)
    }

    private fun olympians(query: TypedQuery<Olympian>) = try {
        query.resultList!!
    } catch (e: EmptyResultDataAccessException) {
        LOG.error("Error while querying", e)
        throw NotFound("no found olympians")
    }

}