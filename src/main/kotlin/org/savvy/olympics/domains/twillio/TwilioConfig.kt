package org.savvy.olympics.domains.twillio

import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class TwilioConfig {

    @Value("\${twilio.account-sid}")
    lateinit var accountSid: String

    @Value("\${twilio.auth-token}")
    lateinit var authToken: String

    @Value("\${twilio.service-id}")
    lateinit var serviceId: String
    @PostConstruct
    fun initializeTwilio() {
        println("Initializing Twilio! AccountSID: $accountSid, AuthToken: $authToken")
        Twilio.init(accountSid, authToken)
    }
}