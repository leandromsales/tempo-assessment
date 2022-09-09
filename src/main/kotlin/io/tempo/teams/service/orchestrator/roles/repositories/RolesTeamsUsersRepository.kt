package io.tempo.teams.service.orchestrator.roles.repositories

import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsersId
import org.apache.catalina.User
import org.springframework.data.jpa.repository.JpaRepository

interface RolesTeamsUsersRepository : JpaRepository<RolesTeamsUsers, RolesTeamsUsersId> {

    fun getRolesTeamsUsersById_TeamIdAndId_UserId(teamId: String, userId: String): RolesTeamsUsers

    fun getRolesTeamsUsersById_RoleId(roleId: Long): List<RolesTeamsUsers>

    fun getRolesTeamsUsersById_TeamId(teamId: String): List<RolesTeamsUsers>

}