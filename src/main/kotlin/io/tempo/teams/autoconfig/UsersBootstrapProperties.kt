package io.tempo.teams.autoconfig

import io.tempo.teams.service.orchestrator.roles.models.Role
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@Component
@ConfigurationProperties(UsersBootstrapProperties.PROPERTY_ROOT_PREFIX)
class UsersBootstrapProperties {

    var quantity: Long = 0

    companion object {
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap.users"
    }

}
