package io.tempo.teams.service.orchestrator.roles

import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import io.tempo.teams.service.AbstractController
import io.tempo.teams.service.orchestrator.roles.models.Role
import io.tempo.teams.service.orchestrator.roles.models.RoleMembership
import io.tempo.teams.service.orchestrator.teams.TeamsController
import io.tempo.teams.util.validators.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity

@Tags(value = [Tag(name = "RolesController")])
@CrossOrigin("*")
@RestController
class RolesController : AbstractController() {

    companion object {
        val LOG = KotlinLogging.logger {}
        const val ROLES_BASE = "/roles"
        const val ROLES_WITH_ID_PATH_VARIABLE = "$ROLES_BASE/{id}"
        const val ROLES_WITH_TEAMS = "$ROLES_BASE/{teamId}"
        const val ROLES_WITH_TEAMS_AND_USERS = "$ROLES_WITH_TEAMS/users/{userId}"
    }

    @Autowired
    private lateinit var rolesService: RolesService

    @Operation(summary = "getAll")
    @GetMapping(value = [ ROLES_BASE], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "All roles.")
    ])
    @JsonView(View.Public::class)
    fun getAll(): List<Role?> {
        return this.rolesService.getAll()
    }

    @Operation(summary = "get")
    @RequestMapping(value = [ ROLES_WITH_ID_PATH_VARIABLE], method = [RequestMethod.GET], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Role is found by the given id.")
    ])
    @JsonView(View.Internal::class)
    fun get(@Valid @PathVariable("id") id: Long): Role {
        return rolesService.get(id)
    }

    @Operation(summary = "addRole")
    @PostMapping(value = [ ROLES_BASE], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Role is added.")
    ])
    fun add(@Validated(RolesPost::class) @RequestBody role: Role): Role {
        return rolesService.save(role)
    }

    @Operation(summary = "updateRole")
    @PatchMapping(ROLES_WITH_ID_PATH_VARIABLE)
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "If role is successfully updated.")
    ])
    fun update(@PathVariable("id") id: Long,
               @Validated(RolesPatch::class) @RequestBody(required = false) role: Role? = null): Role? {
        LOG.debug { "Role id=$id with fragment present=$role" }
        LOG.info { "Role fragment to update is fragment=${role}" }
        role?.let {
            it.id = id
            return rolesService.save(it)
        }
        return null
    }

    @Operation(summary ="deleteRole", description = "Delete role.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The deleted role."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @DeleteMapping(value = [ROLES_WITH_ID_PATH_VARIABLE])
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        rolesService.delete(id)
        return makeResponse()
    }

    @Operation(summary = "setUserRole", description = "Set user role.")
    @RequestMapping(value = [TeamsController.TEAMS_USERS_ROLES], method = [RequestMethod.POST], produces = ["application/json"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Role is set to the User for this team.")
    ])
    fun setUserRole(@Valid @PathVariable("id") id: String,
                    @Valid @PathVariable("userId") userId: String,
                    @Valid @RequestParam("roleId") roleId: Long) {
        rolesService.setUserRole(id, userId, roleId)
    }

    @Operation(summary ="getMembership", description = "A Membership role of a user in a team.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user role."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @GetMapping(value = [ROLES_WITH_TEAMS_AND_USERS])
    fun lookupUserRole(@PathVariable("teamId") teamId: String, @PathVariable("userId") userId: String): Role? {
        return rolesService.getUserRole(teamId, userId)
    }

    @Operation(summary ="getMembership", description = "A Membership role of a user in a team.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user role."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @GetMapping(value = [ROLES_WITH_ID_PATH_VARIABLE])
    fun lookupMembershipOfRole(@PathVariable("id") id: Long): List<RoleMembership>? {
        return rolesService.getRoleMembership(id)
    }

}