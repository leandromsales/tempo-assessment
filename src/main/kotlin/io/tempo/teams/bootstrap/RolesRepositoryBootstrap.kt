package io.tempo.teams.bootstrap

import io.tempo.teams.autoconfig.RolesBootstrapProperties
import io.tempo.teams.service.orchestrator.roles.RolesService
import io.tempo.teams.service.orchestrator.roles.models.Role
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.lang.Exception
import javax.annotation.PostConstruct

@Component(value = "RolesRepositoryBootstrap")
@ConditionalOnProperty(
    value = [RolesBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
class RolesRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    protected lateinit var properties: RolesBootstrapProperties

    @Autowired
    protected lateinit var rolesService: RolesService

    @PostConstruct
    private fun setupInitialData() {
        LOG.debug("Bootstrapping roles data into the database at {}", dataSourceProperties?.url)

        LOG.debug("  -> Starting bootstrap of Roles.")
        LOG.debug("  -> Roles Bootstrap data: {}", properties.bootstrapInstances)

        properties.bootstrapInstances.forEach { instance: Role ->
            try {
                LOG.debug("  -> Saving instance {}: ", instance)
                val rolesSavedInstance = rolesService.save(instance)
                LOG.debug("  -> Saved with id {}.", rolesSavedInstance.id)

            } catch (errorSaving: DataIntegrityViolationException) {
                if (errorSaving.cause?.cause?.message!!.contains("duplicate")) {
                    LOG.debug("  -> Instance {} already stored in the database. Continuing...", instance)
                } else {
                    throw errorSaving
                }
            } catch (otherErrors: Exception) {
                LOG.error("  -> Error loading bootstrap instance {}: {}", instance, otherErrors)
                throw otherErrors
            }
        }
        LOG.info("Finished Bootstrapping of Roles Repository.")
    }
}