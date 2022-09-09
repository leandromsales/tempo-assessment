package io.tempo.teams.service.orchestrator.teams.models

import com.fasterxml.jackson.databind.util.StdConverter
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
import io.tempo.teams.service.orchestrator.users.UsersService
import io.tempo.teams.service.orchestrator.users.models.User
import org.springframework.beans.factory.annotation.Autowired

class TeamLeadDeserializer : StdConverter<String, User>() {

    @Autowired
    lateinit var usersService: UsersService

    override fun convert(value: String?): User? {
        return usersService.get(value!!)
    }


}