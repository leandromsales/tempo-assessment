package io.tempo.teams.service.orchestrator.users.repositories

import io.tempo.teams.service.orchestrator.users.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<User, String>