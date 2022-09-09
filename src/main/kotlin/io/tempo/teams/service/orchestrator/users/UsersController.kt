package io.tempo.teams.service.orchestrator.users

import com.fasterxml.jackson.annotation.JsonView
import io.tempo.teams.service.AbstractController
import io.tempo.teams.service.orchestrator.users.models.*
import io.tempo.teams.util.validators.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tags(value = [Tag(name = "UserServiceClient")])
@CrossOrigin("*")
@RestController
class UsersController : AbstractController() {

    companion object {
        const val USER_BASE = "/users"
    }

    @Autowired
    private lateinit var usersService: UsersService

    @Operation(summary ="getAll", description = "Get the list of users.")
    @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "The list of users.")
    ])
    @GetMapping(value = [USER_BASE])
    @JsonView(View.Public::class)
    fun getAll(): ResponseEntity<Any> {
        return makeResponse(usersService.getAllUsers())
    }

    @Operation(summary ="get", description = "Get a user by id.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user."),
        ApiResponse(responseCode = "404", description = "User not found."),
    ])
    @GetMapping(value = [ "$USER_BASE/{id}" ])
    @JsonView(View.Internal::class)
    fun get(@PathVariable("id") id: String): Any {
        return usersService.get(id) ?: "null"
    }

    @Operation(summary ="add", description = "Add a user.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The added user instance.",
            headers = [ Header(name ="Location", schema = Schema(implementation = String::class)) ]),
        ApiResponse(responseCode = "409", description = "Invalid parameter (e.g externalId already in use)."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @PostMapping(value = [USER_BASE])
    fun add(@Validated(UsersPost::class) @RequestBody user: User): ResponseEntity<Any> {
        var addedUser = usersService.add(user)
        return if (addedUser != null) {
            val headers = mutableMapOf("Location" to "$USER_BASE/${addedUser.id}")
            makeResponse(addedUser, headers)
        } else {
            val headers = mutableMapOf<String, String>()
            makeResponse("null", headers)
        }
    }

    @Operation(summary ="update", description = "Update a user.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The updated user."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @PatchMapping(value = ["${USER_BASE}/{id}"])
    fun update(@PathVariable("id") id: String, @Validated(UsersPatch::class) @RequestBody user: User): User {
        user.id = id
        return usersService.update(user)
    }

    @Operation(summary ="delete", description = "Delete user.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The deleted user."),
        ApiResponse(responseCode = "500", description = "Internal Server Error.")
    ])
    @DeleteMapping(value = ["${USER_BASE}/{id}"])
    fun delete(@PathVariable("id") id: String): ResponseEntity<Any> {
        usersService.delete(id)
        return makeResponse()
    }
}
