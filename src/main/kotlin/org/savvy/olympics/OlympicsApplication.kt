package org.savvy.olympics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication(scanBasePackages = ["org.savvy.olympics"])
@ConfigurationPropertiesScan
class OlympicsApplication

fun main(args: Array<String>) {
	val context: ApplicationContext = runApplication<OlympicsApplication>(*args)
}

