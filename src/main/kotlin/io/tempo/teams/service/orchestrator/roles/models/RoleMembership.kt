package io.tempo.teams.service.orchestrator.roles.models

import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.models.User


data class RoleMembership (val user: User, val team: Team)