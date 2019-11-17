package org.zenbot.szolnok.timetable.backend.service.jobs

/**
 * Indicates that there is no such batch for the supplied parameters
 */
class NoSuchBatchJobException(
    override val message: String
) : RuntimeException(message)
