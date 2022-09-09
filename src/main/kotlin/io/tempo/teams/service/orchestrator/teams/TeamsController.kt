package io.tempo.teams.service.orchestrator.teams

import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import io.tempo.teams.service.AbstractController
import io.tempo.teams.service.orchestrator.roles.models.Role
import io.tempo.teams.service.orchestrator.roles.models.RoleMembership
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.models.User
import io.tempo.teams.util.validators.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity

@Tags(value = [Tag(name = "TeamsController")])
@CrossOrigin("*")
@RestController
class TeamsController : AbstractController() {

    companion object {
        val LOG = KotlinLogging.logger {}
        const val TEAMS_BASE = "/teams"
        const val TEAMS_WITH_ID_PATH_VARIABLE = "$TEAMS_BASE/{id}"
        const val TEAMS_USERS = "$TEAMS_BASE/{id}/users"
        const val TEAMS_ROLES = "$TEAMS_BASE/{id}/roles"
    }

    @Autowired
    private lateinit var teamsService: TeamsService

    @Operation(summary = "getAll")
    @GetMapping(value = [ TEAMS_BASE], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "All teams.")
    ])
    @JsonView(View.Public::class)
    fun getAll(): List<Team?> {
        return this.teamsService.getAll()
    }

    @Operation(summary = "get")
    @RequestMapping(
        value = [TEAMS_WITH_ID_PATH_VARIABLE],
        method = [RequestMethod.GET],
        produces = ["application/json"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Team is found by the given id.")
        ]
    )
    @JsonView(View.Internal::class)
    fun get(@Valid @PathVariable("id") id: String): Team {
        return teamsService.get(id)
    }

    @Operation(summary = "addTeam")
    @PostMapping(value = [ TEAMS_BASE], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Team is added.")
    ])
    fun add(@Validated(TeamsPost::class) @RequestBody team: Team): Team {
        return teamsService.save(team)
    }

    @Operation(summary = "update")
    @PatchMapping(TEAMS_WITH_ID_PATH_VARIABLE)
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "If team is successfully updated.")
    ])
    fun update(@PathVariable("id") id: String,
               @Validated(TeamsPatch::class) @RequestBody(required = false) team: Team? = null): Team? {
        LOG.debug { "Team id=$id with fragment present=$team" }
        LOG.info { "Team fragment to update is fragment=${team}" }
        team?.let {
            it.id = id
            return teamsService.save(it)
        }
        return null
    }

    @Operation(summary ="delete", description = "Delete team.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The deleted team."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @DeleteMapping(value = [TEAMS_WITH_ID_PATH_VARIABLE])
    fun delete(@PathVariable("id") id: String): ResponseEntity<Any> {
        teamsService.delete(id)
        return makeResponse()
    }

    @Operation(summary = "addUserToTeam", description = "Add user to a team.")
    @PostMapping(value = [ TEAMS_ROLES], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User is added to the Team.")
    ])
    fun addUserToTeam(@Valid @PathVariable("id") id: String,
                      @Validated(RoleMembershipPost::class) @RequestBody roleMembership: RoleMembership) {
        LOG.info { "role id: ${roleMembership.role}" }
        if (roleMembership.role == null) roleMembership.role = Role()
        teamsService.addUserToTeam(id, roleMembership.user?.id!!, roleMembership.role?.id!!)
    }

    @Operation(summary = "removeUserToTeam", description = "Remove user from a team.")
    @DeleteMapping(value = [ TEAMS_ROLES], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User is removed from the Team.")
    ])
    fun removeUserFromTeam(@Valid @PathVariable("id") id: String,
                           @Validated(RoleMembershipPost::class) @RequestBody roleMembership: RoleMembership) {
        teamsService.removeUserFromTeam(id, roleMembership.user?.id!!)
    }

    @Operation(summary = "getAllUsers", description = "Get all users of a given team.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "All team's users"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    ])
    @RequestMapping(value = [TEAMS_USERS ],
                    method = [RequestMethod.GET],
                    produces = ["application/json"])
    @JsonView(View.Public::class)
    fun getAllUsers(@Valid @PathVariable("id") id: String) : List<User>? {
        return teamsService.getAllUsers(id)
    }

}