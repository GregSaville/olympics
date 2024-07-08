package org.savvy.olympics.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface HealthCheckExchange {
    @GetMapping("/katherine")
    fun healthCheck(): ResponseEntity<String>
}

@Controller
class HealthCheck : HealthCheckExchange {
    override fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("She is the coolest bestest girlfriend in the entire world <#")
    }

}