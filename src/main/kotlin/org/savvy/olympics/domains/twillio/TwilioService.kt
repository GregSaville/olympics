package org.savvy.olympics.domains.twillio

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import org.savvy.olympics.api.exceptions.NotFound
import org.savvy.olympics.domains.services.UserService
import org.savvy.olympics.repos.repositories.UserRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TwilioService(
    private val userRepo: UserRepo,
){

    @Value("\${twilio.service-id}")
    lateinit var serviceId: String

    fun sendVerification(phoneNumber: String): Verification {
        try {
            val formattedNumber = formatPhoneNumber(phoneNumber)
            println("Sending Verification Code to $phoneNumber")
            return Verification.creator(
                serviceId, formattedNumber, "sms"
            ).create()
       } catch (e: Exception) {
            println("Error sending verification check: ${e.message}")
            throw e
        }
    }

    @Transactional
    fun checkVerification(phoneNumber: String, code: String): Boolean {
        try {
            val formattedPhoneNumber = formatPhoneNumber(phoneNumber)

            // Check the verification code
            val verificationCheck = VerificationCheck.creator(serviceId, code)
                .setTo(formattedPhoneNumber)
                .create()

            return when(verificationCheck.valid) {
                true -> {

                    val user = userRepo.findByPhoneNumber(phoneNumber) ?: throw NotFound("user $phoneNumber")
                    user.verifiedOtp = code

                    println("VerificationCheck SID: ${verificationCheck.sid}")
                    true
                }

                false -> {
                    println("Failed Verification Check for Number $phoneNumber")
                    false
                }
            }


        } catch (e: Exception) {
            throw e
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return if (!phoneNumber.startsWith("+")) "+1$phoneNumber" else phoneNumber
    }
}