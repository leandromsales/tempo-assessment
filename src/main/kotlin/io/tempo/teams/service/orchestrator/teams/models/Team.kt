package io.tempo.teams.service.orchestrator.teams.models

import io.tempo.teams.util.validators.nullornotblank.NullOrNotBlank
import io.tempo.teams.util.validators.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.tempo.teams.service.orchestrator.roles.models.RolesTeamsUsers
import io.tempo.teams.service.orchestrator.users.models.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import mu.KotlinLogging
import javax.annotation.PostConstruct

@Entity(name = "teams")
@Table(indexes = [
        Index(name = "teams_name_idx", columnList = "name", unique = true)
])
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Team {

        companion object {
                val LOG = KotlinLogging.logger {}
        }

        @Id
        @NotNull(groups = [ TeamsPatch::class, RoleMembershipPost::class ], message = "Field 'id' must be provided.")
        @Column
        @JsonView(View.Public::class)
        var id: String? = null

        @field: NotBlank(message = "Field 'name' must be provided.", groups = [ TeamsPost::class ])
        @field: NullOrNotBlank(message = "Field 'name' must be provided.", groups = [ TeamsPatch::class ])
        @Column(nullable = false)
        @JsonView(View.Public::class)
        var name: String? = null

        @JsonView(View.Internal::class)
        @JsonSerialize(converter = TeamLeadSerializer::class)
        @JsonDeserialize(converter = TeamLeadDeserializer::class)
        @JsonProperty(value = "teamLeadId")
        @OneToOne(optional = true, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "teams_users_teamLeader_id_fk"))
        var teamLead: User? = null

        @OneToMany(cascade = [ CascadeType.ALL ], fetch = FetchType.LAZY, mappedBy = "team", orphanRemoval = true)
        @PrimaryKeyJoinColumn
        @JsonView(View.Internal::class)
        @JsonSerialize(converter = TeamMemberIdsSerializer::class)
        var teamMemberIds: MutableList<RolesTeamsUsers> = mutableListOf()

        fun updateNotNullFields(team: Team) {
                this.updateNotNullFields(team.id, team.name, team.teamLead)
        }

        fun updateNotNullFields(id: String?, name: String?, teamLead: User?) {
                if (this.id == null) {
                        this.id = id
                }
                name?.let { this.name = it }
                teamLead?.let { this.teamLead = it }
        }

        override fun toString(): String {
                return "Team(id=$id, name='$name', teamLeadId='$teamLead', teamMembers='$teamMemberIds')"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || javaClass != other.javaClass) return false
                val that = other as Team
                return id == that.id
        }

        override fun hashCode(): Int {
                return Objects.hash(id)
        }

}

