package org.savvy.olympics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OlympicsApplication

fun main(args: Array<String>) {
	runApplication<OlympicsApplication>(*args)
}

