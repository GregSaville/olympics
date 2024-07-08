package org.savvy.olympics.domains

import org.savvy.olympics.repos.entities.User
import org.savvy.olympics.repos.repositories.UserRepo
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepo: UserRepo
) {

    fun findUser(phoneNumber: String?): User? {
        return userRepo.findById(phoneNumber ?: "null").getOrNull()
    }

    fun createUser(user: User): User {
        return userRepo.save(user)
    }
}