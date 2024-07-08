package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepo : JpaRepository<User, String>