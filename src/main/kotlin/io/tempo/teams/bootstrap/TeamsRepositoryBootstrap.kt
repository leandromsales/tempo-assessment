package io.tempo.teams.service.bootstrap

import io.tempo.teams.autoconfig.TeamsBootstrapProperties
import io.tempo.teams.bootstrap.AbstractRepositoryBootstrap
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.teams.TeamsService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.lang.Exception
import javax.annotation.PostConstruct

/**
 * In-memory repository for testing tickets to be used by the app
 *
 * @author marcellodesales
 * @author leandromsales
 *
 */
@Component(value = "TeamsRepositoryBootstrap")
@ConditionalOnProperty(
    value = [TeamsBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
class TeamsRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    protected lateinit var properties: TeamsBootstrapProperties

    @Autowired
    protected lateinit var teamsService: TeamsService

    @PostConstruct
    private fun setupInitialData() {
        LOG.debug("Bootstrapping teams data into the database at {}", dataSourceProperties?.url)

        LOG.debug("  -> Starting bootstrap of Teams.")
        LOG.debug("  -> Team Bootstrap data: {}", properties?.bootstrapInstances)

        properties?.bootstrapInstances?.forEach { instance: Team ->
            try {
                LOG.debug("  -> Saving instance {}: ", instance)
                val teamSavedInstance = teamsService.save(instance)
                LOG.debug("  -> Saved with id {}.", teamSavedInstance.id)

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
        LOG.info("Finished Bootstrapping of Teams Repository.")
    }
}