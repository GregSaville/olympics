package org.savvy.olympics.domains.services

import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.repos.entities.User
import org.savvy.olympics.repos.repositories.UserRepo
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo
) {

    fun findUser(phoneNumber: String?): User? {
        return phoneNumber?.let {
            userRepo.findByPhoneNumber(it)
        }
    }

    fun createUser(user: User): User {
        OlympicsLogger.log.enter(log = Log(
            location = UserService::class.java.name,
            message = "New User Created! Welcome ${user}")
        )
        return userRepo.save(user)
    }

    fun updateUser(user: User): User {
        OlympicsLogger.log.enter(log = Log(
            location = UserService::class.java.name,
            message = "$user updated!")
        )
        return userRepo.save(user)
    }

    fun findAllUsers(): List<User> {
        return userRepo.findAll().toList()
    }

    fun findAllParticipants(): List<User> {
        return userRepo.findParticipants()
    }
 }