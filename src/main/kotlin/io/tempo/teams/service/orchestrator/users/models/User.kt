package io.tempo.teams.service.orchestrator.users.models

import io.tempo.teams.util.validators.*
import io.tempo.teams.util.validators.notblankwithminmax.NotBlankWithMinMax
import io.tempo.teams.util.validators.nullornotblank.NullOrNotBlank
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import io.tempo.teams.service.core.exceptions.Errors
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@Entity(name = "users")
@Table(indexes = [
    Index(name = "user_firstName_idx", columnList = "firstName", unique = false),
    Index(name = "user_lastName_idx", columnList = "lastName", unique = false),
])
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer"])
class User {

    @Id
    @NotNull(groups = [ UsersPatch::class, TeamsPost::class, TeamsPatch::class, RoleMembershipPost::class ],
        message = "Field 'id' must be provided.")
    @Column
    @JsonView(View.Public::class)
    var id: String? = null

    @NotBlankWithMinMax(min = 3, max = 300, message = "The field 'firstName' must have at least 3 chars and max " +
            "300 chars.", groups = [ UsersPost::class, UserPut::class ])
    @NullOrNotBlank(min = 3, max = 300, message = "The field 'firstName' must have at least 3 chars and max " +
            "300 chars.", groups = [ UsersPatch::class ])
    @Column(nullable = true, columnDefinition = "varchar(300)")
    @JsonView(View.Internal::class)
    var firstName: String? = null
        set(value) {
            field = value
            this.displayName = value?.lowercase()?.replace(" ", "") +
                    this.lastName?.replaceFirstChar { it.uppercase() }
        }

    @NotBlankWithMinMax(min = 3, max = 300, message = "The field 'lastName' must have at least 3 chars and max " +
            "300 chars.", groups = [ UsersPost::class, UserPut::class ])
    @NullOrNotBlank(min = 3, max = 300, message = "The field 'lastName' must have at least 3 chars and max " +
            "300 chars.", groups = [ UsersPatch::class ])
    @Column(nullable = true, columnDefinition = "varchar(300)")
    @JsonView(View.Internal::class)
    var lastName: String? = null
        set(value) {
            field = value
            this.displayName = this.firstName?.lowercase() +
                    value?.replaceFirstChar { it.uppercase() }?.replace(" ", "")
        }

    @Column(nullable = true, columnDefinition = "varchar(500)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(View.Public::class)
    var displayName: String? = null
        private set

    @Column(nullable = true, columnDefinition = "varchar(500)")
    @JsonView(View.Internal::class)
    var avatarUrl: String? = null
        set(value) {
            try {
                URL(value)

            } catch (e: MalformedURLException) {
                throw Errors.GENERAL_VALUE_INVALID.exception("Invalid URL for 'avatarUrl'.")
            }
            field = value
        }

    @Column(nullable = true, columnDefinition = "varchar(100)")
    @JsonView(View.Internal::class)
    var location: String? = null

    fun updateNotNullFields(user: User) {
        this.updateNotNullFields(user.id, user.firstName, user.lastName, user.avatarUrl, user.location)
    }

    fun updateNotNullFields(id: String?, firstName: String?, lastName: String?, avatarUrl: String?, location: String?) {
        if (this.id == null) {
            this.id = id
        }
        firstName?.let { this.firstName = it }
        lastName?.let { this.lastName = it }
        avatarUrl?.let { this.avatarUrl = it }
        location?.let { this.location = it }
    }

    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, avatarUrl=$avatarUrl)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}