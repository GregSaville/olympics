package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.Team
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TeamRepo {
    fun createTeam(team: Team): Team

    fun findTeamByPhone(phoneNumber: String): Team?

    fun findAll(): List<Team>

    fun findById(id: UUID): Team?

    fun delete(teamId: UUID)

    fun delete(teamIds: List<UUID>)
}