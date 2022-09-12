package io.tempo.teams.service.orchestrator.teams.models

data class TeamPOJO (
    val id: String,
    val name: String,
    val teamLeadId: String?,
    val teamMemberIds: MutableList<String> = mutableListOf()
)