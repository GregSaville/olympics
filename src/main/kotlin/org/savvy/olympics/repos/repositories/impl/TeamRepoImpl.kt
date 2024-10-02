package org.savvy.olympics.repos.repositories.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.repos.entities.Team
import org.savvy.olympics.repos.repositories.TeamRepo
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import java.util.*

@Component
class TeamRepoImpl(
    private val entityManager: EntityManager
) : TeamRepo {

    companion object {
        val LOG = LoggerFactory.getLogger(TeamRepoImpl::class.java)
    }

    override fun createTeam(team: Team): Team {
        LOG.debug("Saving Team ${team.id}")
        return entityManager.merge(team)
    }

    override fun findTeamByPhone(phoneNumber: String): Team? {
        LOG.debug("Searching For Team with number: $phoneNumber")
        val phoneParam = "phoneParam"

        val queryStr = """
            SELECT t FROM Team t 
            LEFT JOIN t.playerOne oOne 
            LEFT JOIN t.playerTwo oTwo 
            WHERE oOne.phoneNumber = :phoneParam OR oTwo.phoneNumber = :phoneParam
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Team::class.java)
            .setParameter(phoneParam, phoneNumber)

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            LOG.warn("No Team found with phone number: $phoneNumber")
            null
        }
    }

    override fun findAll(): List<Team> {
        LOG.debug("Finding Teams!")

        val queryStr = """
            SELECT t FROM Team t
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Team::class.java)

        return teams(query)
    }

    override fun findById(id: UUID): Team? {
        LOG.debug("Searching For Team with id: $id")
        val idParam = "idParam"

        val queryStr = """
            SELECT t FROM Team t 
            WHERE t.id = :idParam
        """.trimIndent()

        val query = entityManager.createQuery(queryStr, Team::class.java)
            .setParameter(idParam, id)

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            LOG.warn("No Team found with id: $id")
            null
        }
    }

    override fun delete(teamId: UUID) {
        LOG.debug("Deleting Team with id: $teamId")
        val idParam = "idParam"

        val queryStr = """
            DELETE FROM Team 
            WHERE id = :idParam
        """.trimIndent()

       entityManager.createQuery(queryStr)
            .setParameter(idParam, teamId)
           .executeUpdate()
    }

    override fun delete(teamIds: List<UUID>) {
        teamIds.forEach { delete(it) }
    }

    private fun teams(query: TypedQuery<Team>) = try {
        query.resultList!!
    } catch (e: EmptyResultDataAccessException) {
        UserRepoImpl.LOG.error("Error while querying", e)
        throw NotFound("no found teams")
    }
}