package org.savvy.olympics.api

import org.savvy.olympics.domains.services.UserService
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.repos.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("/register", accept = [MediaType.APPLICATION_JSON_VALUE])
interface RegisterExchange {

    @GetMapping("/all")
    fun getAllUsers():ResponseEntity<List<UserDto>>

    @GetMapping
    fun getRegistrationDetails(
        @RequestBody request: GetRegistrationRequest?
    ) : ResponseEntity<UserDto>

    @PostMapping
    fun submitRegistration(
        @RequestBody request: SubmitRegistrationRequest
    ): ResponseEntity<List<UserDto>>

    @PutMapping
    fun updateRegistration(
        @RequestBody request: UpdateRegistrationRequest
    ): ResponseEntity<List<UserDto>>
}

@RestController
class RegisterController : RegisterExchange {

    companion object {
        val location: String = this::class.java.name
    }

    @Autowired
    lateinit var userService: UserService
    override fun getAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(userService.findAllUsers().toDtos())
    }

    override fun getRegistrationDetails(request: GetRegistrationRequest?): ResponseEntity<UserDto> {
        val phoneNumber = request?.phoneNumber
        when (val user = userService.findUser(phoneNumber)) {
            null -> {
                return if(phoneNumber == null) {
                    ResponseEntity.notFound().build()
                } else {
                    ResponseEntity.ok(
                        userService.createUser(
                            User(
                                phoneNumber = phoneNumber
                            )
                        ).toDto()
                    )
                }
            }
            else -> return ResponseEntity.ok(user.toDto())
        }
    }

    override fun submitRegistration(request: SubmitRegistrationRequest): ResponseEntity<List<UserDto>> {
        if (request.username.isBlank()) return ResponseEntity.badRequest().build()
        when (val user = userService.findUser(request.phoneNumber)) {
            null -> {
                // Create the main user
                val createdUser = userService.createUser(
                    User(
                        userName = request.username,
                        phoneNumber = request.phoneNumber
                    )
                )
                request.plusOne?.let { plusOneRequest ->
                    val guest = userService.findUser(plusOneRequest.plusOnesNumber)
                    if (guest == null) {
                        val plusOne = userService.createUser(
                            User(
                                phoneNumber = plusOneRequest.plusOnesNumber
                            )
                        )

                        createdUser.assignPlusOne(plusOne)
                        return ResponseEntity.ok(listOf(createdUser, plusOne).toDtos())
                    } else {
                        return ResponseEntity.ok(listOf(createdUser, guest).toDtos())
                    }
                }

                return ResponseEntity.ok(listOf(createdUser).toDtos())
            }
            else -> {
                OlympicsLogger.log.enter(log = Log(location = location, message = "User Found ${user.userName} Updating their name to ${request.username}"))
                user.userName = request.username
                request.plusOne?.let { plusOneRequest ->
                    val guest = userService.findUser(plusOneRequest.plusOnesNumber)

                    if (guest == null) {
                        val plusOne = userService.createUser(
                            User(
                                phoneNumber = plusOneRequest.plusOnesNumber
                            )
                        )

                        createdUser.assignPlusOne(plusOne)
                        return ResponseEntity.ok(listOf(createdUser, plusOne).toDtos())
                    }

                    user.assignPlusOne(guestUser)
                    userService.updateUser(user)
                    userService.updateUser(guestUser)

                    return ResponseEntity.ok(listOf(user, guestUser).toDtos())
                }

                return ResponseEntity.ok(listOf(user).toDtos())
            }
        }
    }

    override fun updateRegistration(request: UpdateRegistrationRequest): ResponseEntity<List<UserDto>> {
        when (val user = userService.findUser(request.phoneNumber)) {
            null -> {
                return ResponseEntity.notFound().build()
            }
            else -> {
                user.userName = request.username
                userService.updateUser(user)
                request.plusOne?.let { plusOneRequest ->
                    val guest = userService.findUser(plusOneRequest.plusOnesNumber)
                    if (guest == null) {
                        userService.createUser(
                            User(
                                phoneNumber = plusOneRequest.plusOnesNumber
                            )
                        )
                    }
                    val guestUser = userService.findUser(plusOneRequest.plusOnesNumber)
                        ?: userService.createUser(User(phoneNumber = plusOneRequest.plusOnesNumber))
                    user.assignPlusOne(guestUser)
                    userService.updateUser(user)
                    userService.updateUser(guestUser)

                    return ResponseEntity.ok(listOf(user, guestUser).toDtos())
                }
                return ResponseEntity.ok(listOf(user).toDtos())
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
    val plusOne: CreatePlusOneRequest?
)

data class CreatePlusOneRequest(
    val plusOnesNumber: String,
    val username: String?
)

data class UpdatePlusOneRequest(
    val plusOnesNumber: String,
    val username: String,
    val participating: Boolean,
)

data class UpdateRegistrationRequest(
    val phoneNumber: String,
    val username: String,
    val participating: Boolean,
    val plusOne: UpdatePlusOneRequest?
)

data class UserDto(
    val phoneNumber: String,
    val username: String?,
    val partner: PartnerDto? = null
)

data class PartnerDto(
    val username: String?,
    val phoneNumber: String
)

fun List<User>.toDtos(): List<UserDto> = map { it.toDto() }

fun User.toDto() = UserDto(
    username = userName,
    phoneNumber = phoneNumber,
    partner = plusOne?.toPartnerDto()
)

fun User.toPartnerDto() = PartnerDto(
    username = userName,
    phoneNumber = phoneNumber
)