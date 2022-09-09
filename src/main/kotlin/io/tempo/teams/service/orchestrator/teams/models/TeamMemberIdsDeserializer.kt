package io.tempo.teams.service.orchestrator.teams.models
//
//import com.fasterxml.jackson.core.JsonParser
//import com.fasterxml.jackson.databind.DeserializationContext
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer
//import com.fasterxml.jackson.databind.node.ObjectNode
//import com.fasterxml.jackson.databind.util.StdConverter
//import io.tempo.teams.service.orchestrator.roles.RolesService
//import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
//import io.tempo.teams.service.orchestrator.teams.TeamsService
//import io.tempo.teams.service.orchestrator.users.UsersService
//import mu.KotlinLogging
//import org.springframework.beans.factory.annotation.Autowired
//
//class TeamMemberIdsDeserializer : StdDeserializer<Team>() {
//
//    private companion object {
//        val LOG = KotlinLogging.logger {}
//    }
//
//    @Autowired
//    lateinit var rolesService: RolesService
//
//    @Autowired
//    lateinit var usersService: UsersService
//
//    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Team {
//        val team = Team()
//        val jsonNode = p.readValueAsTree<ObjectNode>()
//        val customIdNode = jsonNode.get("id")
//        val id = customIdNode.asText()
//
//        if (id != null) {
//            team.id = id
//        }
//
//        val customNameNode = jsonNode.get("name")
//        val name = customNameNode.asText()
//        team.name = name
//
//        val customTeamLeadIdNode = jsonNode.get("teamLeadId")
//        val teamLeadId = customTeamLeadIdNode.asText()
//        if (teamLeadId != null) {
//            val teamLead = usersService.get(teamLeadId)
//            team.teamLead = teamLead
//        }
//
//        val customTeamMemberIdsNode = jsonNode.get("teamMemberIds")
//        val teamMemberIds = customTeamMemberIdsNode.asIterable()
//        teamMemberIds.forEach { userId: String ->
//            try {
//                val roleTeamUser = rolesService.getRoleTeamUser(team.id!!, userId)
//                team.teamMemberIds.add(roleTeamUser!!)
//
//            } catch (e: Exception) {
//                LOG.debug {
//                    "Ignoring unknown relation role-team-user for teamId '${team.id}' " +
//                            " and userId '${userId}'"
//                }
//            }
//        }
//
//        return team
//    }
//
//
//}