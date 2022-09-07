package io.tempo.teams.service.orchestrator.teams.repositories

import io.tempo.teams.service.orchestrator.teams.models.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamsRepository : JpaRepository<Team, String>