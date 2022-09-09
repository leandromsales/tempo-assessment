package io.tempo.teams.service.orchestrator.teams

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.core.exceptions.Errors
import io.tempo.teams.service.orchestrator.roles.RolesService
import io.tempo.teams.service.orchestrator.users.models.User
import io.tempo.teams.service.orchestrator.teams.repositories.TeamsRepository
import io.tempo.teams.service.orchestrator.teams.models.*
import io.tempo.teams.service.orchestrator.users.UsersService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
@Transactional(rollbackFor = [TempoException::class], propagation = Propagation.REQUIRED)
class TeamsService {

    private companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Autowired
    private lateinit var teamsRepository: TeamsRepository

    @Autowired
    private lateinit var rolesService: RolesService

    @Autowired
    private lateinit var usersService: UsersService

    @PostConstruct
    fun init() {
        LOG.debug { "Initializing the Teams' Service..." }
        LOG.debug { "Loaded the Teams Service" }
    }

    fun getAll(): List<Team?> {
        return teamsRepository.findAll()
    }

    fun get(id: String): Team {
        try {
            return teamsRepository.findById(id).get()

        } catch (e: java.lang.Exception) {
            throw Errors.TEAM_NOT_FOUND.exception("Id: $id", e)
        }
    }

    @Throws(TempoException::class, TempoException::class)
    fun getReference(id: String): Team {
        return try {
            teamsRepository.getReferenceById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.TEAM_NOT_FOUND.exception("Team not found with id $id: ${e.message}.", e)
        }
    }

    @Throws(TempoException::class)
    fun save(team: Team) : Team {

        var savedTeam: Team? = null

        team.id?.let {
            savedTeam = try {
                get(it)

            } catch (e: Exception) {
                null
            }
        }

        if (savedTeam == null) {
            savedTeam = team
            LOG.debug { "Team added: $savedTeam" }

        } else {
            LOG.debug { "Found team for updating: $savedTeam" }
            savedTeam!!.updateNotNullFields(team)
            LOG.debug { "Team updated: $savedTeam" }
        }

        try {
            savedTeam = teamsRepository.save(savedTeam!!)

        } catch (e: java.lang.Exception) {
            val message = "Error while trying to save team: ${e.message}"
            LOG.debug { message }
            throw Errors.TEAM_SAVE_ERROR.exception(message, e)
        }

        return savedTeam!!
    }

    @Throws(TempoException::class)
    fun delete(id: String) {
        try {
            teamsRepository.deleteById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.TEAM_DELETE_FAILURE.exception("Error while deleting team with id '${id}': " +
                    "${e.message}", e)
        }
    }

    @Throws(TempoException::class)
    fun addUserToTeam(id: String, userId: String, roleId: Long = 0L) {
        return rolesService.setUserRole(get(id), usersService.get(userId)!!, roleId)
    }

    @Throws(TempoException::class)
    fun removeUserFromTeam(id: String, userId: String) {
        return rolesService.removeUserRole(get(id), usersService.get(userId)!!)
    }

    fun getAllUsers(teamId: String): List<User>? {
        return rolesService.getAllUsersOfTeam(teamId)
    }
}