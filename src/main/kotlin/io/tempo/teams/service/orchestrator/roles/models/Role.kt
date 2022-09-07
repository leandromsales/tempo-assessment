package io.tempo.teams.service.orchestrator.roles.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import io.tempo.teams.util.validators.*
import io.tempo.teams.util.validators.nullornotblank.NullOrNotBlank
import mu.KotlinLogging
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity(name = "roles")
@Table(indexes = [
    Index(name = "role_name_idx", columnList = "name", unique = true)]
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Role {

    companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = [ RolesPatch::class ], message = "Field 'id' must be provided.")
    @Column(nullable = true, updatable = true, unique = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(View.Public::class)
    var id: Long? = null

    @field: NotBlank(message = "Field 'name' must be provided.", groups = [ RolesPost::class ])
    @field: NullOrNotBlank(message = "Field 'name' must be provided.", groups = [ RolesPatch::class ])
    @Column(nullable = false)
    @JsonView(View.Public::class)
    var name: String? = null

    fun updateNotNullFields(role: Role) {
        this.updateNotNullFields(role.id, role.name)
    }

    fun updateNotNullFields(id: Long?, name: String?) {
        if (this.id == null) {
            this.id = id
        }
        name?.let { this.name = it }
    }

}