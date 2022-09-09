package io.tempo.teams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@EnableFeignClients
class Application


fun main(args: Array<String>) {
    runApplication<Application>(*args)
}