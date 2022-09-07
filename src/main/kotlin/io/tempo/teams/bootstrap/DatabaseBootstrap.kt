package io.tempo.teams.bootstrap

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Load database properties
 *
 * @author leandromsales
 *
 */
@Component
class DatabaseBootstrap {

    private val LOG = KotlinLogging.logger {}

    @Autowired
    private val dataSourceProperties: DataSourceProperties? = null

    @PostConstruct
    private fun setupInitialData() {
        LOG.debug("Bootstrapping database settings")
        LOG.debug("  --> Database URL: {}", dataSourceProperties?.url ?: "not defined.")
        LOG.debug("  --> Database Driver: {}", dataSourceProperties?.driverClassName ?: "not defined.")
    }
}