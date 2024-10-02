package org.savvy.olympics.api

import jakarta.persistence.NoResultException
import org.savvy.olympics.api.exceptions.BadRequest
import org.savvy.olympics.api.exceptions.NotFound
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val LOG = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(NoResultException::class)
    fun handleNoResultException(ex: NoResultException): ResponseEntity<String> {
        LOG.error("No result found: ${ex.message}", ex)
        return ResponseEntity("No result found", HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        LOG.error("An error occurred: ${ex.message}", ex)
        return ResponseEntity("An internal error occurred :(", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NotFound::class)
    fun handleGenericException(ex: NotFound): ResponseEntity<String> {
        LOG.info("No resource found", ex)
        return ResponseEntity("No resource found for ${ex.message}", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadRequest::class)
    fun handleGenericException(ex: BadRequest): ResponseEntity<String> {
        LOG.info("Bad Request", ex)
        return ResponseEntity("Bad Request: ${ex.message}", HttpStatus.BAD_REQUEST)
    }
}