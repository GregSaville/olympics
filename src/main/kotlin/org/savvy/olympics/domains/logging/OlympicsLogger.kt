package org.savvy.olympics.domains.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.Date
import java.util.UUID

object OlympicsLogger {
    val log = LoggerFactory.getLogger(this::class.java)
    private var _sequence: Int = 0
    val sequence: Int
        get() = ++_sequence
    val systemLogs : MutableMap<LogId, LogSpecs> = mutableMapOf()
}


data class Log(
    val id: LogId = LogId.create(),
    val location: String,
    val error: String? = null,
    val message: String? = null,
    val extraContext: Map<String, Any>? = null
)

data class LogSpecs(
    val location: String,
    val error: String? = null,
    val message: String? = null,
    val extraContext: Map<String, Any>? = null
)

data class LogId(
    val value: String
) {

    companion object {
        fun create(value: String? = null): LogId {
            return LogId(value ?: "LOG-${Int.MIN_VALUE.plus(OlympicsLogger.sequence)}-${Instant.now()}")
        }
    }
}


fun Logger.enter(logs: List<Log>? = null, log: Log? = null) {
    logs?.forEach {

        OlympicsLogger.systemLogs[it.id] = it.toSpec()

        OlympicsLogger.log.info(it.toMessage())

    }
    log?.let {

        OlympicsLogger.systemLogs[it.id] = it.toSpec()

        OlympicsLogger.log.info(it.toMessage())

    }
}

fun Log.toMessage(): String = "${id.value} || $message || $error || Extra Context ${extraContext.toString()}"

fun Log.toSpec() = LogSpecs(
    location = location,
    error = error,
    message = message,
    extraContext = extraContext
)