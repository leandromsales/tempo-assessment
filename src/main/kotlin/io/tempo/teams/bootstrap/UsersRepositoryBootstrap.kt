package io.tempo.teams.bootstrap

import io.tempo.teams.autoconfig.UsersBootstrapProperties
import io.tempo.teams.clients.UsersApiClient
import io.tempo.teams.service.orchestrator.users.UsersService
import io.tempo.teams.service.orchestrator.users.models.User
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component(value = "UsersRepositoryBootstrap")
@ConditionalOnProperty(
    value = [UsersBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
class UsersRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    private lateinit var usersBootstrapProperties: UsersBootstrapProperties

    @Autowired
    private lateinit var usersService: UsersService

    @Autowired
    private lateinit var usersApiClient: UsersApiClient

    @PostConstruct
    private fun setupInitialData() {

        LOG.debug("Bootstrapping user data into the database at {}", this.dataSourceProperties?.url)

        val users = usersApiClient.getAll()

        LOG.debug("  -> Loading ${usersBootstrapProperties.quantity} of ${users.size} users from Tempo database via API...")

        var i = 0L
        run usersLoop@ {
            users.forEach { user: User ->
                i++

                try {
                    LOG.debug("Importing User $i of min(${users.size}, ${usersBootstrapProperties.quantity})...")
                    val userDetailed = usersApiClient.get(user.id!!)
                    usersService.add(userDetailed)

                } catch (e: Exception) {
                    LOG.error("  -> Error loading bootstrap instance $user: ${e.message}")
                    throw e
                }

                if (i == usersBootstrapProperties.quantity) return@usersLoop
            }
        }

        LOG.info("Finished Bootstrapping of Users Repository.")
    }

}