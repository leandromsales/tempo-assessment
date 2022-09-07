package io.tempo.teams.autoconfig

import io.tempo.teams.service.orchestrator.roles.models.Role
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@Component
@ConfigurationProperties(RolesBootstrapProperties.PROPERTY_ROOT_PREFIX)
class RolesBootstrapProperties {

    var bootstrapInstances: List<Role> = listOf()

    companion object {
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap.roles"
    }

    override fun toString(): String {
        return "RolesBootstrapProperties{" +
                ", bootstrapInstances='" + bootstrapInstances + '\'' +
                ", " + super.toString() +
                '}'
    }
}
