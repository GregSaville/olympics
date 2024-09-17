package org.savvy.olympics.api

import org.springframework.http.HttpStatus
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
        val htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Katherine's Health Check</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        text-align: center;
                        background-color: #f4f4f4;
                        padding: 50px;
                    }
                    h1 {
                        color: #FF4081;
                        font-size: 2.5em;
                    }
                    p {
                        color: #333;
                        font-size: 1.2em;
                    }
                </style>
            </head>
            <body>
                <h1>Hi Katherine!</h1>
                <p>You are the coolest, bestest girlfriend in the entire world</p>
                <p>Stay awesome!</p>
                <p> Love Gregory <3</p>
            </body>
            </html>
        """.trimIndent()

        return ResponseEntity.ok(htmlContent)
    }
}
