package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.Olympian
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepo {
    fun findByPhoneNumber(phoneNumber: String): Olympian?

    fun findByIds(userIds: List<UUID>): List<Olympian>

     fun findParticipants(): List<Olympian>

     fun save(olympian: Olympian): Olympian

     fun findAllUsers(): List<Olympian>
}