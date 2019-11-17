package org.zenbot.szolnok.timetable.backend.service.jobs

/**
 * Indicates that the given type of batch is already running
 */
class BatchJobAlreadyRunningException(
    override val message: String
) : RuntimeException(message)
