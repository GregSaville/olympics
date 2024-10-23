package org.savvy.olympics.domains.services

import jakarta.transaction.Transactional
import org.savvy.olympics.api.exceptions.BadRequest
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.repos.entities.Team
import org.savvy.olympics.repos.repositories.TeamRepo
import org.springframework.stereotype.Component
import java.util.*

@Component
class TeamService(
    private val teamRepo: TeamRepo,
    private val userService: UserService
) {

    @Transactional
    fun findByNumber(phoneNumber: String): Team? {
        return teamRepo.findTeamByPhone(phoneNumber)
    }

    @Transactional
    fun getAll(): List<Team> {
       val result = teamRepo.findAll()
        return result
    }

    @Transactional
    fun getJoinable(): List<Team> {
        return teamRepo.findJoinable()
    }

    @Transactional
    fun create(team: Team): Team {
        return teamRepo.createTeam(team)
    }

    @Transactional
    fun joinTeam(teamId: UUID, userId: UUID): Team {

        val team = teamRepo.findById(teamId) ?: throw NotFound(teamId.toString())

        val user = userService.findUsers(listOf(userId)).firstOrNull() ?: throw NotFound(userId.toString())

        if (team.playerOne == user) throw BadRequest("Duplicate User")

        if (team.playerTwo != null) throw BadRequest("Team Full")

        return teamRepo.update(team.copy(playerTwo = user))
    }

    @Transactional
    fun findById(teamId: UUID): Team? {
        return teamRepo.findById(teamId)
    }

    @Transactional
    fun update(team: Team): Team? {
        return teamRepo.update(team)
    }

    @Transactional
    fun leaveTeam(phoneNumber: String) {
        val teamToLeave = teamRepo.findTeamByPhone(phoneNumber) ?: throw NotFound("No team for phone number $phoneNumber")

        when {
            teamToLeave.playerOne.phoneNumber == phoneNumber -> {
                if (teamToLeave.playerTwo != null) {
                    // If playerTwo exists, promote them to playerOne
                    teamToLeave.playerOne = teamToLeave.playerTwo!!
                    teamToLeave.playerTwo = null
                } else {
                    // If there's no playerTwo, delete the team
                    teamRepo.delete(teamToLeave.id)
                    return
                }
            }
            teamToLeave.playerTwo?.phoneNumber == phoneNumber -> {
                // If playerTwo is leaving, simply set playerTwo to null
                teamToLeave.playerTwo = null
            }
            else -> throw NotFound("Player not found in the team")
        }

        teamRepo.update(teamToLeave)
    }


    @Transactional
    fun merge(teamOneId: UUID, teamTwoId: UUID): Team {
        val teamOne = teamRepo.findById(teamOneId) ?: throw NotFound("Team $teamOneId")

        val teamTwo = teamRepo.findById(teamOneId) ?: throw NotFound("Team $teamOneId")

        if(teamOne.playerTwo == null && teamTwo.playerTwo == null) {
            val newTeam = Team(
                playerOne = teamOne.playerOne,
                playerTwo = teamTwo.playerOne
            )

            teamRepo.delete(listOf(teamOneId, teamTwoId))
            teamRepo.createTeam(newTeam)
            return newTeam
        } else throw BadRequest("No Team Switch when having a partner")
    }
}