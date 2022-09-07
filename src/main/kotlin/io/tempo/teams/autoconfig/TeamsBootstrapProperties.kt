package io.tempo.teams.autoconfig

import io.tempo.teams.service.orchestrator.teams.models.Team
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@Component
@ConfigurationProperties(TeamsBootstrapProperties.PROPERTY_ROOT_PREFIX)
class TeamsBootstrapProperties {

    var bootstrapInstances: List<Team> = listOf()

    companion object {
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap.teams"
    }

    override fun toString(): String {
        return "TeamsBootstrapProperties{" +
                ", bootstrapInstances='" + bootstrapInstances + '\'' +
                ", " + super.toString() +
                '}'
    }
}
