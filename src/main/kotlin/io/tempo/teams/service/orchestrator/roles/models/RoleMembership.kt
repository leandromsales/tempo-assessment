package io.tempo.teams.service.orchestrator.roles.models

import com.fasterxml.jackson.annotation.JsonInclude
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.models.User
import io.tempo.teams.util.validators.RoleMembershipPost
import java.util.*
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class RoleMembership (

    @NotNull(message = "Field 'user' must be provided.", groups = [RoleMembershipPost::class])
    val user: User? = null,

    val team: Team? = null,

    var role: Role? = null

)