package io.tempo.teams.clients;

import io.tempo.teams.service.orchestrator.teams.models.Team
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "teamsApiClient", url = "\${io.tempo.teams.clients.teams.url}")
interface TeamsApiClient {

    @GetMapping(value = [ "" ],
                consumes = [ MediaType.APPLICATION_JSON_VALUE ],
                produces = [ MediaType.APPLICATION_JSON_VALUE ])
    fun getAll(): MutableList<Team>

    @GetMapping(value = [ "/{id}" ],
            consumes = [ MediaType.APPLICATION_JSON_VALUE ],
            produces = [ MediaType.APPLICATION_JSON_VALUE ])
    fun get(@PathVariable id: String): Team

}