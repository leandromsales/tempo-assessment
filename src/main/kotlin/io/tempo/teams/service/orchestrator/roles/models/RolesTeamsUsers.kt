package io.tempo.teams.service.orchestrator.roles.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.teams.models.TeamsUsersId
import io.tempo.teams.service.orchestrator.users.models.User
import io.tempo.teams.util.validators.*
import io.tempo.teams.util.validators.nullornotblank.NullOrNotBlank
import mu.KotlinLogging
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity(name = "roles_teams_users")
@Table(indexes = [
    Index(name = "user_team_idx", columnList = "user_id,team_id", unique = true)]
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class RolesTeamsUsers {

    companion object {
        val LOG = KotlinLogging.logger {}
    }

    @EmbeddedId
    var id: RolesTeamsUsersId = RolesTeamsUsersId()

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @MapsId(value = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = ForeignKey(name = "roles_teams_users_user_fk"))
    var user: User? = null

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @MapsId(value = "teamId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = ForeignKey(name = "roles_teams_users_team_fk"))
    var team: Team? = null

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @MapsId(value = "roleId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = ForeignKey(name = "roles_teams_users_role_fk"))
    var role: Role? = null

}