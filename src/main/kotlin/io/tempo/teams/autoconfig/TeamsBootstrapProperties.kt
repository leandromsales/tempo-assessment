package io.tempo.teams.autoconfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@Component
@ConfigurationProperties(TeamsBootstrapProperties.PROPERTY_ROOT_PREFIX)
class TeamsBootstrapProperties {

    var quantity: Long = 0

    companion object {
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap.teams"
    }

}
