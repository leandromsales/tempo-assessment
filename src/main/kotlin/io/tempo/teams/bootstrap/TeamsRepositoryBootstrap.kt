package io.tempo.teams.bootstrap

import io.tempo.teams.autoconfig.TeamsBootstrapProperties
import io.tempo.teams.clients.TeamsApiClient
import io.tempo.teams.service.core.exceptions.Errors
import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.teams.models.TeamPOJO
import io.tempo.teams.service.orchestrator.users.UsersService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component(value = "TeamsRepositoryBootstrap")
@ConditionalOnProperty(
    value = [TeamsBootstrapProperties.PROPERTY_ROOT_PREFIX + ".bootstrapData"],
    havingValue = "true",
    matchIfMissing = false
)
@DependsOn(value = [ "UsersRepositoryBootstrap" ])
class TeamsRepositoryBootstrap : AbstractRepositoryBootstrap() {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    private lateinit var teamsBootstrapProperties: TeamsBootstrapProperties

    @Autowired
    private lateinit var teamsService: TeamsService

    @Autowired
    private lateinit var userService: UsersService

    @Autowired
    private lateinit var teamsApiClient: TeamsApiClient

    @PostConstruct
    private fun setupInitialData() {

        LOG.debug("Bootstrapping team data into the database at {}", this.dataSourceProperties?.url)

        val teams = teamsApiClient.getAll()

        LOG.debug("  -> Loading ${teams.size} teams from Tempo database via API...")

        var i = 0L
        run teamsLoop@{
            teams.forEach { team: TeamPOJO ->
                i++

                try {
                    LOG.debug("(Team $i of [max(${teams.size}, ${teamsBootstrapProperties.quantity})]) Adding team ${team.id}")
                    val teamDetailed = teamsApiClient.get(team.id!!)

                    val teamToSave = Team()
                    teamToSave.id = teamDetailed.id
                    teamToSave.name = teamDetailed.name
                    if (teamDetailed.teamLeadId != null) {
                        teamToSave.teamLead = userService.get(teamDetailed.teamLeadId)
                    }
                    val teamSaved = teamsService.save(teamToSave)
                    for (teamMemberId in teamDetailed.teamMemberIds) {
                        LOG.debug("Adding user '$teamMemberId' to Team '${team.id}'")
                        teamsService.addUserToTeam(teamSaved.id!!, teamMemberId)
                    }

                } catch (te: TempoException) {
                    if (te.model.getErrorCode() != Errors.ROLE_NOT_FOUND.value &&
                        te.model.getErrorCode() != Errors.USER_NOT_FOUND.value) {

                        LOG.error("  -> Error saving team instance $team: ${te.message}")
                        throw te
                    }

                } catch (e: Exception) {
                    LOG.error("  -> Error loading bootstrap instance $team: ${e.message}")
                    throw e
                }

                if (i == teamsBootstrapProperties.quantity) return@teamsLoop
            }
        }

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