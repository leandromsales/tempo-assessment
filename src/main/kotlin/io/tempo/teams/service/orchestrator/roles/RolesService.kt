package io.tempo.teams.service.orchestrator.roles

import io.tempo.teams.service.core.exceptions.Errors
import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.orchestrator.roles.models.Role
import io.tempo.teams.service.orchestrator.roles.models.RoleMembership
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsersId
import io.tempo.teams.service.orchestrator.roles.repositories.RolesRepository
import io.tempo.teams.service.orchestrator.roles.repositories.RolesTeamsUsersRepository
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.users.UsersService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
@Transactional(rollbackFor = [TempoException::class], propagation = Propagation.REQUIRED)
class RolesService {

    private companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Autowired
    private lateinit var rolesRepository: RolesRepository

    @Autowired
    private lateinit var rolesTeamsUsersRepository: RolesTeamsUsersRepository

    @Autowired
    private lateinit var teamsService: TeamsService

    @Autowired
    private lateinit var usersService: UsersService

    @PostConstruct
    fun init() {
        LOG.debug { "Initializing the Roles' Service... Creating configs..." }
        LOG.debug { "Loaded the Roles Service" }
    }

    fun getAll(): List<Role?> {
        return rolesRepository.findAll()
    }

    fun get(id: Long): Role {
        try {
            return rolesRepository.findById(id).get()

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Id: $id", e)
        }
    }

    @Throws(TempoException::class)
    fun getReference(id: Long): Role {
        return try {
            rolesRepository.getReferenceById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Role not found with id $id: ${e.message}.", e)
        }
    }

    @Throws(TempoException::class)
    fun save(role: Role): Role {

        var savedRole: Role? = null

        role.id?.let {
            savedRole = try {
                get(it)

            } catch (e: Exception) {
                null
            }
        }

        if (savedRole == null) {
            savedRole = role
            LOG.debug { "Role added: $savedRole" }

        } else {
            LOG.debug { "Found Role for updating: $savedRole" }
            savedRole!!.updateNotNullFields(role)
            LOG.debug { "Role updated: $savedRole" }
        }

        try {
            savedRole = rolesRepository.save(savedRole!!)

        } catch (e: java.lang.Exception) {
            val message = "Error while trying to save Role: ${e.message}"
            LOG.debug { message }
            throw Errors.ROLE_SAVE_ERROR.exception(message, e)
        }

        return savedRole!!
    }

    @Throws(TempoException::class)
    fun delete(id: Long) {
        try {
            rolesRepository.deleteById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_DELETE_FAILURE.exception(
                "Error while deleting Role with id '${id}': " +
                        "${e.message}", e
            )
        }
    }

    @Throws(TempoException::class)
    fun setUserRole(teamId: String, userId: String, roleId: Long) {

        var rolesTeamsUsers = try {
            getRoleTeamUser(teamId, userId)

        } catch (e: Exception) {
            null
        }

        var role: Role? = null
        try {
            role = this.getReference(roleId)

        } catch (e: Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Role id: '${roleId}'")
        }

        if (rolesTeamsUsers != null) {
            rolesTeamsUsers.id.roleId = roleId
            rolesTeamsUsers.role = role

        } else {
            val user = usersService.getReference(userId)
            val team = teamsService.getReference(teamId)
            rolesTeamsUsers = RolesTeamsUsers()
            rolesTeamsUsers.id = RolesTeamsUsersId(userId, teamId, roleId)
            rolesTeamsUsers.user = user
            rolesTeamsUsers.team = team
            rolesTeamsUsers.role = role
        }

        try {
            rolesTeamsUsersRepository.save(rolesTeamsUsers)

        } catch (e: Exception) {
            throw Errors.ROLE_SET_USER.exception(
                "Error while setting Role '${roleId}' to team '${teamId}' for user '${userId}': " +
                        "${e.message}", e
            )
        }
    }

    @Throws(TempoException::class)
    fun getRoleTeamUser(teamId: String, userId: String): RolesTeamsUsers? {
        return try {
            rolesTeamsUsersRepository.getRolesTeamsUsersById_TeamIdAndId_UserId(teamId, userId)

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Role not found for user '${userId}' in the team '${teamId}'")
        }
    }

    @Throws(TempoException::class)
    fun getUserRole(teamId: String, userId: String): Role? {
        return try {
            getRoleTeamUser(teamId, userId)!!.role

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Role not found for user '${userId}' in the team '${teamId}'")
        }
    }

    @Throws(TempoException::class)
    fun getRoleMembership(roleId: Long): List<RoleMembership>? {
        val result = mutableListOf<RoleMembership>()

        return try {
            val rolesTeamsUsers = rolesTeamsUsersRepository.getRolesTeamsUsersById_RoleId(roleId)
            rolesTeamsUsers.forEach { instance: RolesTeamsUsers ->
                result.add(RoleMembership(instance.user!!, instance.team!!))
            }
            result

        } catch (e: java.lang.Exception) {
            throw Errors.ROLE_NOT_FOUND.exception("Role not found for id '${roleId}'")
        }
    }

}