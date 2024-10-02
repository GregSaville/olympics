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
        return teamRepo.findAll()
    }

    @Transactional
    fun create(team: Team): Team {
        return teamRepo.createTeam(team)
    }

    @Transactional
    fun findById(teamId: UUID): Team? {
        return teamRepo.findById(teamId)
    }

    @Transactional
    fun leaveTeam(phoneNumber: String) {
        val teamToLeave = teamRepo.findTeamByPhone(phoneNumber) ?: throw NotFound("no team for phone number $phoneNumber")

        if (teamToLeave.playerTwo != null){
            teamToLeave.playerOne = teamToLeave.playerTwo!!
            teamToLeave.playerTwo = null
            return
        } else {
            teamRepo.delete(teamToLeave.id)
            return
        }
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