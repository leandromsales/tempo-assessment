package io.tempo.teams.adaptor.observability

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.*
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.StreamSupport

/**
 * Log all the properties loaded during:
 * 1. When the application loads and the event of refresh configs is called
 * 2. When use use Spring cloud config and the configs change dynamically
 */
@Component(value = "LogResolvedConfigurationProperties")
class PropertiesResolvedLoggerListener {

    // https://github.com/MicroUtils/kotlin-logging
    private val log = KotlinLogging.logger {}

    @EventListener
    fun handleContextRefresh(event: ApplicationEnvironmentPreparedEvent) {

        val env: Environment = event.environment
        log.info("====== Environment and configuration ======")
        log.info("Active profiles: {}", env.activeProfiles.toString())

        val sources: MutablePropertySources = (env as AbstractEnvironment).propertySources
        StreamSupport.stream(sources.spliterator(), false)
            .filter { ps -> ps is EnumerablePropertySource }
            .map { ps -> (ps as EnumerablePropertySource).propertyNames }
            .flatMap(Arrays::stream)
            .sorted()
            .distinct()
//            .filter { prop -> !(prop.contains("credentials") || prop.contains("password")) }
            .forEach { prop -> log.info("{}: {}", prop, env.getProperty(prop)) }
        log.info("===========================================")
    }
}