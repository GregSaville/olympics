package org.savvy.olympics.api

import org.savvy.olympics.domains.UserService
import org.savvy.olympics.repos.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.annotation.HttpExchange
@HttpExchange("/register", accept = [MediaType.APPLICATION_JSON_VALUE])
interface RegisterExchange {
    @GetMapping
    fun getRegistrationDetails(
        @RequestBody request: GetRegistrationRequest?
    ) : ResponseEntity<String>

    @PostMapping
    fun submitRegistration(
        @RequestBody request: SubmitRegistrationRequest
    ): ResponseEntity<List<User>>
}


@RestController
class RegisterController : RegisterExchange {

    @Autowired
    lateinit var userService: UserService

    override fun getRegistrationDetails(getRegistrationRequest: GetRegistrationRequest?): ResponseEntity<String> {
        val phoneNumber = getRegistrationRequest?.phoneNumber
        when (val user = userService.findUser(phoneNumber)) {
            null -> {
                return if(phoneNumber == null) {
                    ResponseEntity.ok("Hello New User")
                } else {
                    ResponseEntity.ok(
                        userService.createUser(
                            User(
                                phoneNumber = phoneNumber
                            )
                        ).toString()
                    )
                }
            }
            else -> return ResponseEntity.ok("Welcome Back ${user.userName ?: user.phoneNumber}")
        }
    }

    override fun submitRegistration(request: SubmitRegistrationRequest): ResponseEntity<List<User>> {
        return when (val user = userService.findUser(request.phoneNumber)) {
            null -> {
                val createdUser = userService.createUser(
                    User(
                        userName = request.username,
                        phoneNumber = request.phoneNumber
                    )
                )
                val guest = userService.findUser(request.plusOne?.plusOnesNumber)

                if (guest == null) {
                    userService.createUser(
                        User(
                            phoneNumber = request.plusOne!!.plusOnesNumber
                        )
                    )
                }
                guest?.let {
                    ResponseEntity.ok(listOf(createdUser, guest))
                } ?: ResponseEntity.ok(listOf(createdUser))
            }
            else -> {
                user.userName = request.username
                if(request.plusOne != null) {
                    //Todo Implement partner logic
                }
                return ResponseEntity.ok(listOf(user))
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
    val plusOne: PlusOneRequest?
)

data class PlusOneRequest(
    val plusOnesNumber: String
)