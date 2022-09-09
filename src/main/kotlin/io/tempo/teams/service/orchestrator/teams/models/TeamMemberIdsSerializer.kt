package io.tempo.teams.service.orchestrator.teams.models

import com.fasterxml.jackson.databind.util.StdConverter
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers

class TeamMemberIdsSerializer : StdConverter<List<RolesTeamsUsers>, MutableList<String>>() {

    override fun convert(value: List<RolesTeamsUsers>?): MutableList<String> {
        val result: MutableList<String> = mutableListOf()

        value?.forEach { roleTeamUser: RolesTeamsUsers ->
            result.add(roleTeamUser.id.userId!!)
        }

        return result
    }


}