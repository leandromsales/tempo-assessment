package io.tempo.teams.service.orchestrator.users

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.core.exceptions.Errors
import io.tempo.teams.service.orchestrator.users.models.*
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import javax.annotation.PostConstruct

@Service
@Transactional(rollbackFor = [TempoException::class], propagation = Propagation.REQUIRED)
class UsersService {

    private companion object {
        private val LOG = KotlinLogging.logger {}
    }

    @Autowired
    private lateinit var usersRepository: UsersRepository

    @Autowired
    private lateinit var teamsService: TeamsService

    @PostConstruct
    fun init() {
        LOG.debug { "Initializing the Users' Service... Creating configs..." }
        LOG.debug { "Loaded the Teams Service" }
    }

    @Throws(TempoException::class, IllegalArgumentException::class, TempoException::class)
    fun add(user: User, teams: Set<Team>?): User? {
        LOG.debug { "New add user request: $user $teams" }
        val foundUser = try {
            user.id?.let { get(it) }

        } catch (e: java.lang.Exception) {
            null
        }

        if (foundUser != null) {
            throw Errors.USER_ID_ALREADY_IN_USE.exception("User already exists, use update request to " +
                    "update user info.")
        }

        var addedUser = save(user)

        val userTeams = teamsService.addUserToTeams(teams, addedUser)
        LOG.info("Saved user-teams object: $userTeams")

        try {
            addedUser = save(addedUser)

        } catch (e: java.lang.Exception) {
            throw Errors.USER_INVALID.exception("Erro ao adicionar usuário.", e)
        }

        LOG.debug("  -> Added user: {}.", addedUser)
        return addedUser
    }

    @Throws(TempoException::class, IllegalArgumentException::class)
    fun update(user: User, teams: Set<Team>?) : User {
        if (StringUtils.isBlank(user.id)) {
            throw Errors.USER_NOT_FOUND.exception("Id must be specified.")
        }

        val founduser = get(user.id!!)

        LOG.info("Found user: $founduser")
        LOG.info("Data to be updated: $user")

        if (founduser?.id?.trim() != "" && user.id != null
            && user.id?.trim() != "" && founduser?.id != user.id) {
            throw Errors.USER_UPDATE_FAILURE.exception("Could not update user due to missing parameter.")
        }

        founduser?.updateNotNullFields(user)

        var updateduser = save(founduser!!)

        val userTeams = teamsService.addUserToTeams(teams, updateduser)
        LOG.info("Saved user-teams object: $userTeams")

        updateduser = save(updateduser)
        LOG.debug("  -> Updated user: {}.", updateduser)

        return updateduser
    }

    @Throws(TempoException::class)
    fun getAllUsers(): List<User> {
        return usersRepository.findAll()
    }

    @Throws(TempoException::class)
    fun get(id: String): User? {
        return try {
            LOG.debug { "Searching for user with ID='${id}'" }
            usersRepository.findById(id).get()

        } catch (e: java.lang.Exception) {
            LOG.error { "Couldn't find user by Id='${id}': ${e.message}" }
//            throw Errors.USER_NOT_FOUND.exception("Id: $id", e)
            return null
        }
    }

    @Throws(TempoException::class, TempoException::class)
    fun getReference(id: String): User {
        return try {
            usersRepository.getReferenceById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.USER_NOT_FOUND.exception("User not found with id $id: ${e.message}.", e)
        }
    }

    @Throws(TempoException::class)
    fun save(user: User): User {
        return try {
            usersRepository.save(user)

        } catch (e: java.lang.Exception) {
            throw Errors.USER_UPDATE_FAILURE.exception("Error while adding or updating user of internal id " +
                    "'${user.id}' and externalId '${user.id}': ${e.message}", e)
        }
    }

    @Throws(TempoException::class)
    fun delete(id: String) {
        try {
            usersRepository.deleteById(id)

        } catch (e: java.lang.Exception) {
            throw Errors.USER_DELETE_FAILURE.exception("Error while deleting user with id '${id}': " +
                    "${e.message}", e)
        }
    }

}

