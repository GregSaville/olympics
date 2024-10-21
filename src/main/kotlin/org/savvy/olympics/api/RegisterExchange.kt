package org.savvy.olympics.api

import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.domains.services.UserService
import org.savvy.olympics.domains.services.TeamService
import org.savvy.olympics.domains.twillio.TwilioService
import org.savvy.olympics.repos.entities.Olympian
import org.savvy.olympics.repos.entities.Team
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.annotation.HttpExchange
import java.util.*

@HttpExchange(accept = [MediaType.APPLICATION_JSON_VALUE])
interface RegisterExchange {

    @GetMapping("/register/all")
    fun getAllUsers():ResponseEntity<List<UserDto>>

    @GetMapping("/register")
    fun getRegistrationDetails(
        @RequestBody request: GetRegistrationRequest?
    ) : ResponseEntity<UserDto>

    @PostMapping("/register")
    fun submitRegistration(
        @RequestBody request: SubmitRegistrationRequest
    ): ResponseEntity<TeamDto>

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest
    ): ResponseEntity<UserDto>
}

@RestController
class RegisterController : RegisterExchange {

    companion object {
        val location: String = this::class.java.name
    }

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var teamService: TeamService

    @Autowired
    lateinit var twilioService: TwilioService

    override fun getAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(userService.findAllUsers().toDtos())
    }

    override fun getRegistrationDetails(request: GetRegistrationRequest?): ResponseEntity<UserDto> {
        val phoneNumber = request?.phoneNumber
        return when (val user = userService.findUser(phoneNumber)) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(user.toDto())
        }
    }

    override fun submitRegistration(request: SubmitRegistrationRequest): ResponseEntity<TeamDto> {
        if (request.username.isBlank()) return ResponseEntity.badRequest().build()
        // Find or create the main user (Olympian)
        val user = userService.findUser(request.phoneNumber) ?: userService.createUser(
            Olympian(
                userName = request.username,
                phoneNumber = request.phoneNumber,
                participating = request.participating
            )
        )

        val userTeam = teamService.findByNumber(user.phoneNumber)

        if(userTeam != null) {
            return ResponseEntity.ok(userTeam.toDto())
        }

        // If a plus one is provided, find or create the second Olympian (plus one)
        val team = request.plusOne?.let { plusOneRequest ->
            val plusOne = userService.findUser(plusOneRequest.phoneNumber) ?: userService.createUser(
                Olympian(
                    phoneNumber = plusOneRequest.phoneNumber,
                    userName = plusOneRequest.username,
                    participating = plusOneRequest.participating
                )
            )

            // Create a new team with the two Olympians
            teamService.create(
                Team(
                    playerOne = user,
                    playerTwo = plusOne
                )
            )
        } ?: run {
            // If no plus one is provided, create a single-player team
            teamService.create(
                Team(
                    playerOne = user,
                    playerTwo = null // No plus one
                )
            )
        }

        return ResponseEntity.ok(team.toDto())
    }

    override fun signIn(request: SignInRequest): ResponseEntity<UserDto> {

        val userAttempting = userService.findUser(request.phoneNumber) ?: throw NotFound("user for number ${request.phoneNumber}")

        userAttempting.verifiedOtp?.let {
            if(it == request.otp)  {
                return ResponseEntity.ok(userAttempting.toDto())
            } else {
                return ResponseEntity.badRequest().build()
            }
        } ?: run {
            request.otp?.let {
                try {
                    val check = twilioService.checkVerification(userAttempting.phoneNumber, request.otp)
                    if (check) {
                        return ResponseEntity.ok(userAttempting.toDto())
                    } else {
                        return ResponseEntity.badRequest().build()
                    }
                } catch (e: Exception) {
                    twilioService.sendVerification(userAttempting.phoneNumber)
                    return ResponseEntity.accepted().build()
                }
            } ?: run {
                return ResponseEntity.badRequest().build()
            }
        }
    }
}



data class GetRegistrationRequest(
    val phoneNumber: String
)

data class SubmitRegistrationRequest(
    val username: String,
    val phoneNumber: String,
    val participating: Boolean,
    val plusOne: CreatePlusOneRequest?
)

data class CreatePlusOneRequest(
    val phoneNumber: String,
    val username: String?,
    val participating: Boolean
)
data class UserDto(
    val phoneNumber: String,
    val username: String?,
    val participating: Boolean,
)

data class PartnerDto(
    val username: String?,
    val phoneNumber: String,
    val participating: Boolean,
)

data class SignInRequest(
    val phoneNumber: String,
    val otp: String?,
)

fun List<Olympian>.toDtos(): List<UserDto> = map { it.toDto() }

fun Olympian.toDto() = UserDto(
    username = userName,
    phoneNumber = phoneNumber,
    participating = participating,
)

fun Olympian.toPartnerDto() = PartnerDto(
    username = userName,
    phoneNumber = phoneNumber,
    participating = participating
)