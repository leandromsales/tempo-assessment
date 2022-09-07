package io.tempo.teams.service.orchestrator.teams.models

import io.tempo.teams.util.validators.nullornotblank.NullOrNotBlank
import io.tempo.teams.util.validators.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import io.tempo.teams.service.orchestrator.users.models.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import mu.KotlinLogging

@Entity(name = "teams")
@Table(indexes = [
        Index(name = "teams_name_idx", columnList = "name", unique = true)]
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Team {

        companion object {
                val LOG = KotlinLogging.logger {}
        }

        @Id
        @NotNull(groups = [ TeamsPatch::class ], message = "Field 'id' must be provided.")
        @Column(nullable = true, updatable = true, unique = true)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonView(View.Public::class)
        var id: String? = UUID.randomUUID().toString()

        @field: NotBlank(message = "Field 'name' must be provided.", groups = [ TeamsPost::class ])
        @field: NullOrNotBlank(message = "Field 'name' must be provided.", groups = [ TeamsPatch::class ])
        @Column(nullable = false)
        @JsonView(View.Public::class)
        var name: String? = null

        @Column(nullable = false)
        @JsonView(View.Internal::class)
        var teamLeadId: String? = null

        fun updateNotNullFields(team: Team) {
                this.updateNotNullFields(team.id, team.name, team.teamLeadId)
        }

        fun updateNotNullFields(id: String?, name: String?, teamLeadId: String?) {
                if (this.id == null) {
                        this.id = id
                }
                name?.let { this.name = it }
                teamLeadId?.let { this.teamLeadId = it }
        }

        override fun toString(): String {
                return "Team(id=$id, name='$name', teamLeadId='$teamLeadId')"
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

