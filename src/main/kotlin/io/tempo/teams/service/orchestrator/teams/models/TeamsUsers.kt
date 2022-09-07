package io.tempo.teams.service.orchestrator.teams.models

import com.fasterxml.jackson.annotation.JsonInclude
import io.tempo.teams.service.orchestrator.users.models.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity(name = "teams_users")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class TeamsUsers (

    @EmbeddedId
    val teamsUsersId: TeamsUsersId = TeamsUsersId(),

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @MapsId(value = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = ForeignKey(name = "teams_users_user_fk"))
    val user: User? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @MapsId(value = "teamId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = ForeignKey(name = "teams_users_team_fk"))
    val team: Team? = null,

    @Transient
    var linked: Boolean = false

) {
    override fun toString(): String {
        return "teamsUsersId=$teamsUsersId"
    }
}
