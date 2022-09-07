package io.tempo.teams.service.orchestrator.roles.repositories

import io.tempo.teams.service.orchestrator.roles.models.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RolesRepository : JpaRepository<Role, Long>