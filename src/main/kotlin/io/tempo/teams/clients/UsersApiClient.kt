package io.tempo.teams.clients;

import io.tempo.teams.service.orchestrator.users.models.User
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "usersApiClient", url = "\${io.tempo.teams.clients.users.url}")
interface UsersApiClient {

    @GetMapping(value = [ "" ],
                consumes = [ MediaType.APPLICATION_JSON_VALUE ],
                produces = [ MediaType.APPLICATION_JSON_VALUE ])
    fun getAll(): MutableList<User>

    @GetMapping(value = [ "/{id}" ],
            consumes = [ MediaType.APPLICATION_JSON_VALUE ],
            produces = [ MediaType.APPLICATION_JSON_VALUE ])
    fun get(@PathVariable id: String): User

}