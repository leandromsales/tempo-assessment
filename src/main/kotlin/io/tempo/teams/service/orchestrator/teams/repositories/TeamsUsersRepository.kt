package io.tempo.teams.service.orchestrator.teams.repositories

import io.tempo.teams.service.orchestrator.teams.models.TeamsUsers
import io.tempo.teams.service.orchestrator.teams.models.TeamsUsersId
import org.springframework.data.jpa.repository.JpaRepository

interface TeamsUsersRepository : JpaRepository<TeamsUsers, TeamsUsersId> {

}