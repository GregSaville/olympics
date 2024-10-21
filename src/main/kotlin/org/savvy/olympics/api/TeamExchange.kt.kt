package org.savvy.olympics.api

import org.savvy.olympics.api.exceptions.BadRequest
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.domains.services.TeamService
import org.savvy.olympics.domains.services.UserService
import org.savvy.olympics.repos.entities.Team
import org.springframework.data.repository.query.Param
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.service.annotation.HttpExchange
import java.util.*

@HttpExchange("/team", accept = [MediaType.APPLICATION_JSON_VALUE])
interface TeamExchange {

    @GetMapping("/all")
    fun getAllTeams(): ResponseEntity<List<TeamDto>>

    @GetMapping
    fun getTeam(
        @Param(value = "teamId") teamId: UUID?,
        @Param(value = "phoneNumber") phoneNumber: String?
    ): ResponseEntity<TeamDto?>

    @PostMapping
    fun createTeam(
        @RequestBody requestBody: CreateTeamRequest
    ): ResponseEntity<TeamDto?>

    @PostMapping("/update")
    fun updateTeam(
        @RequestBody request: UpdateTeamRequest
    ): ResponseEntity<TeamDto?>

    @DeleteMapping
    fun leaveTeam(
        @RequestBody request: LeaveTeamRequest
    ): ResponseEntity<Void>

    @PostMapping("/merge")
    fun mergeTeams(
        @RequestBody request: MergeTeamsRequest
    ): ResponseEntity<TeamDto>
}

@RestController
class TeamController(
    private val teamService: TeamService,
    private val userService: UserService
): TeamExchange {
    override fun getAllTeams(): ResponseEntity<List<TeamDto>> {
        return ResponseEntity.ok(teamService.getAll().toDtos())
    }

    override fun getTeam(teamId: UUID?, phoneNumber: String?): ResponseEntity<TeamDto?> {
        OlympicsLogger.log.enter(log = Log(location = TeamExchange::class.java.name, message = "Looking for team with id $teamId"))
        if (teamId != null) {
            val result = teamService.findById(teamId) ?: throw NotFound("no team for id $teamId")

            return ResponseEntity.ok(result.toDto())
        }

        return if (phoneNumber != null) {
            val result = teamService.findByNumber(phoneNumber) ?: throw NotFound("no team for number $phoneNumber")

            ResponseEntity.ok(result.toDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override fun createTeam(request: CreateTeamRequest): ResponseEntity<TeamDto?> {

        request.playerTwoId?.let {
            val users = userService.findUsers(listOf(request.playerOneId, request.playerTwoId))

            if(users.size != 2) throw BadRequest("User not found")

            users.forEach {
                if (it.team != null) throw BadRequest("Already assigned to team, leave before creating a new team")
            }

            val newTeam = Team(playerOne = users[0], playerTwo = users[1])
            return ResponseEntity.ok(teamService.create(newTeam).toDto())
        } ?: run {
            val user = userService.findUsers(listOf(request.playerOneId)).first()

            if (user.team != null) throw BadRequest("Already assigned to team, leave before creating a new team")

            val newTeam = Team(playerOne = user, playerTwo = null)
            return ResponseEntity.ok(teamService.create(newTeam).toDto())
        }
    }

    override fun updateTeam(request: UpdateTeamRequest): ResponseEntity<TeamDto?> {
        return with(request) {
            val team = teamService.findById(teamId) ?: throw NotFound("Team for id $teamId")

            if (!newTeamName.isNullOrBlank()) {
                team.name = newTeamName
            }
            if (!teamColor.isNullOrBlank()) {
                team.color = teamColor
            }

            val updatedTeam = teamService.update(team) ?: throw NotFound("Team for id $teamId")

            ResponseEntity.ok(updatedTeam.toDto())
        }
    }


    override fun leaveTeam(request: LeaveTeamRequest): ResponseEntity<Void> {

        teamService.leaveTeam(request.phoneNumber)

        return ResponseEntity.noContent().build()
    }

    override fun mergeTeams(request: MergeTeamsRequest): ResponseEntity<TeamDto> {
        val result = teamService.merge(request.teamOneId, request.teamTwoId)

        return ResponseEntity.ok(result.toDto())
    }
}

data class TeamDto(
    val id: UUID,
    val name: String,
    val color: String,
    val playerOne: UserDto,
    val playerTwo: UserDto?
)

data class MergeTeamsRequest(
    val teamOneId: UUID,
    val teamTwoId: UUID,
)

data class LeaveTeamRequest(
    val phoneNumber: String
)

data class CreateTeamRequest(
    val playerOneId: UUID,
    val playerTwoId: UUID?,
    val teamName: String? = null,
    val teamColor: String? = null,
)

data class UpdateTeamRequest(
    val teamId: UUID,
    val newTeamName: String?,
    val teamColor: String?
)

fun List<Team>.toDtos(): List<TeamDto> = this.map { it.toDto() }

fun Team.toDto(): TeamDto = TeamDto(
    id = this.id,
    name = this.name,
    color = this.color,
    playerOne = this.playerOne.toDto(),
    playerTwo = run {
        this.playerTwo?.let {
            return@run it.toDto()
        } ?: return@run null
    })