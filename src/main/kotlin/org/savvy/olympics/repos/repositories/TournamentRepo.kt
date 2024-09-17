package org.savvy.olympics.repos.repositories

import org.savvy.olympics.repos.entities.Tournament
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TournamentRepo : CrudRepository<Tournament, UUID>