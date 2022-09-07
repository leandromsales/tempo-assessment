package io.tempo.teams.autoconfig

import io.tempo.teams.service.orchestrator.users.models.User
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@Component
@ConfigurationProperties(UsersBootstrapProperties.PROPERTY_ROOT_PREFIX)
class UsersBootstrapProperties {

    var bootstrapInstances: List<User> = listOf()

    companion object {
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap.users"
    }

    override fun toString(): String {
        return "UsersBootstrapProperties{" +
                ", bootstrapInstances='" + bootstrapInstances + '\'' +
                ", " + super.toString() +
                '}'
    }
}
