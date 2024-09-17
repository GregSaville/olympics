package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepo : CrudRepository<User, UUID> {
    @Query(value = "select u.id, u.phone_number, u.registration_date, u.user_name, u.plus_one_id, u.participating, u.team_id from users u where u.phone_number = ?1", nativeQuery = true)
    fun findByPhoneNumber(phoneNumber: String): User?

    @Query(value = "select u.id, u.phone_number, u.registration_date, u.user_name, u.plus_one_id, u.participating, u.team_id from users u where u.participating = true ", nativeQuery = true)
    fun findParticipants(): List<User>
}