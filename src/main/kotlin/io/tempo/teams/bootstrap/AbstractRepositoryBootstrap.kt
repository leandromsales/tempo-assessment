package io.tempo.teams.bootstrap

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.stereotype.Component

/**
 * Updater for the instances
 *
 * @author marcellodesales
 *
 */
@Component // https://reflectoring.io/spring-boot-conditionals/ only loads if the property is defined
@ConditionalOnProperty(
    value = [AbstractRepositoryBootstrap.PROPERTY_ROOT_PREFIX + ".enabled"],
    havingValue = "true",
    matchIfMissing = false
)
abstract class AbstractRepositoryBootstrap {

    @Autowired
    protected var dataSourceProperties: DataSourceProperties? = null

    companion object {
        protected val LOG = LoggerFactory.getLogger(AbstractRepositoryBootstrap::class.java)
        const val PROPERTY_ROOT_PREFIX = "io.tempo.teams.bootstrap"
    }
}