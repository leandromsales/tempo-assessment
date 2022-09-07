package io.tempo.teams.bootstrap

import io.tempo.teams.autoconfig.UsersBootstrapProperties
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.models.User
import io.tempo.teams.service.orchestrator.users.UsersService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * In-memory repository for testing tickets to be used by the app
 *
 * @author leandromsales
 *
 */
@Component(value = "UsersRepositoryBootstrap")
@DependsOn(value = ["SimpleTypesEnumsRepositoryBootstrap", "MarketplaceRepositoryBootstrap"])
@ConditionalOnProperty(
    value = [UsersBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
class UsersRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    private lateinit var properties: UsersBootstrapProperties

    @Autowired
    private lateinit var usersService: UsersService

    @Autowired
    private lateinit var teamsService: TeamsService

    @PostConstruct
    private fun setupInitialData() {

        LOG.debug("Bootstrapping user data into the database at {}", dataSourceProperties?.url)

        LOG.debug("  -> Starting to Bootstrap of User Repository...")
        LOG.debug("  -> User Bootstrap data (${properties.bootstrapInstances.size}): ${properties.bootstrapInstances}")

        properties.bootstrapInstances.forEach { user: User ->
//            val teams = teamsService.getMarketplacesByCodeNames(user.marketplaceCodeNames)
//            try {
//                val newUser = add(user, teams)
//                val login = "REVIEW" //user.getPreferredEmail()
//                teams.forEach {
//                    userService.activate(login.confirmationToken!!, it.id!!, false)
//                }
//                val retrievedUser = authenticate(login.email!!, password!!, marketplaces.first().id!!)
//                if (retrievedUser != newUser) { // Check if user is added by authenticating it
//                    val message = "Usuário adicionado não é o mesmo autenticado."
//                    LOG.error("  -> $message")
//                    throw Exception(message)
//                }
                LOG.debug("Teste de autenticação do usuário bootstrap '\${retrievedUser.id}': OK")

//            } catch (externalIdAlreadyInUseException: EntityexternalIdAlreadyInUseException) {
//                    LOG.error("  -> Instance ${user.id} already stored in the database. Updating...")
//                    user.id = externalIdAlreadyInUseException.entity.id
//                    update(user, teams)
//
//            } catch (e: Exception) {
//                LOG.error("  -> Error loading bootstrap instance $user: ${e.message}")
//                throw e
//            }
        }
        LOG.info("Finished Bootstrapping of Users Repository.")
    }

    fun add(user: User, teams: Set<Team>): User? {
        LOG.debug("  -> Preparing instance {} to load...", user)
        val newUser = usersService.add(user, teams)
        LOG.debug("  -> User added: $newUser")
        return newUser
    }

    fun update(user: User, teams: Set<Team>): User? {
        val updatedUser = usersService.update(user, teams)
        LOG.debug("  -> User updated: $updatedUser")
        return updatedUser
    }

    fun delete(id: String) {
        usersService.delete(id)
        LOG.debug("  -> User deleted.")
    }
}