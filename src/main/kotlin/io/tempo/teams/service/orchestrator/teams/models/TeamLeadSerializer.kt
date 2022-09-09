package io.tempo.teams.service.orchestrator.teams.models

import com.fasterxml.jackson.databind.util.StdConverter
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
import io.tempo.teams.service.orchestrator.users.models.User

class TeamLeadSerializer : StdConverter<User, String>() {

    override fun convert(value: User?): String {
        return value?.id!!
    }


}