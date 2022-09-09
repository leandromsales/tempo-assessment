package io.tempo.teams.bootstrap

import io.tempo.teams.autoconfig.TeamsBootstrapProperties
import io.tempo.teams.clients.TeamsApiClient
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.teams.models.Team
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component(value = "TeamsRepositoryBootstrap")
@ConditionalOnProperty(
    value = [TeamsBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
class TeamsRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    private lateinit var teamsService: TeamsService

    @Autowired
    private lateinit var teamsApiClient: TeamsApiClient

    @PostConstruct
    private fun setupInitialData() {

        LOG.debug("Bootstrapping team data into the database at {}", this.dataSourceProperties?.url)

//        val teams = teamsApiClient.getAll()
//
//        LOG.debug("  -> Loading ${teams.size} teams from Tempo database via API...")
//
//        teams.forEach { team: Team ->
//            try {
//                LOG.debug("Adding team ${team.id}")
//                val teamDetailed = teamsApiClient.get(team.id!!)
//                teamsService.save(teamDetailed)
//
//            } catch (e: Exception) {
//                LOG.error("  -> Error loading bootstrap instance $team: ${e.message}")
//                throw e
//            }
//        }

        LOG.info("Finished Bootstrapping of Teams Repository.")
    }

//    fun add(team: Team): Team? {
//        LOG.debug("  -> Preparing instance {} to load...", team)
//        val newTeam = teamsService.add(team)
//        LOG.debug("  -> Team added: $newTeam")
//        return newTeam
//    }
//
//    fun update(team: Team): Team? {
//        val updatedTeam = teamsService.update(team)
//        LOG.debug("  -> Team updated: $updatedTeam")
//        return updatedTeam
//    }
//
//    fun delete(id: String) {
//        teamsService.delete(id)
//        LOG.debug("  -> Team deleted.")
//    }
}