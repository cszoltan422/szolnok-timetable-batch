package org.zenbot.szolnok.timetable.backend.service.jobs

/**
 * Indicates that the given job request has empty parameters
 */
class EmptyJobParametersException(
    override val message: String
) : RuntimeException(message)
