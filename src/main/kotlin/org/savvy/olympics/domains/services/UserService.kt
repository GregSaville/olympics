package org.savvy.olympics.domains.services

import jakarta.transaction.Transactional
import org.savvy.olympics.api.exceptions.BadRequest
import org.savvy.olympics.domains.logging.Log
import org.savvy.olympics.domains.logging.OlympicsLogger
import org.savvy.olympics.domains.logging.enter
import org.savvy.olympics.domains.twillio.TwilioService
import org.savvy.olympics.repos.entities.Olympian
import org.savvy.olympics.repos.repositories.UserRepo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepo: UserRepo,
    private val twilioService: TwilioService,
) {

    @Transactional
    fun findUser(phoneNumber: String?): Olympian? {
        return phoneNumber?.let {
            userRepo.findByPhoneNumber(it)
        }
    }

    @Transactional
    fun createUser(olympian: Olympian): Olympian {
        if(userRepo.findByPhoneNumber(olympian.phoneNumber) != null) {
            OlympicsLogger.log.enter(log = Log(
                location = UserService::class.java.name,
                message = "User already found for phone number ${olympian.phoneNumber}")
            )
            throw BadRequest("User already exists for phone number ${olympian.phoneNumber}")
        } else {

            val check = twilioService.sendVerification(olympian.phoneNumber)

            OlympicsLogger.log.enter(log = Log(
                location = UserService::class.java.name,
                message = "New User Created! Welcome ${olympian.userName ?: olympian.phoneNumber}")
            )
            return userRepo.save(olympian)
        }
    }

    @Transactional
    fun updateUser(olympian: Olympian): Olympian {
        OlympicsLogger.log.enter(log = Log(
            location = UserService::class.java.name,
            message = "$olympian updated!")
        )
        return userRepo.save(olympian)
    }

    @Transactional
    fun findAllUsers(): List<Olympian> {
        return userRepo.findAllUsers().toList()
    }

    @Transactional
    fun findAllParticipants(): List<Olympian> {
        return userRepo.findParticipants()
    }

    @Transactional
    fun findUsers(userIds: List<UUID>): List<Olympian> {
        return userRepo.findByIds(userIds)
    }
 }