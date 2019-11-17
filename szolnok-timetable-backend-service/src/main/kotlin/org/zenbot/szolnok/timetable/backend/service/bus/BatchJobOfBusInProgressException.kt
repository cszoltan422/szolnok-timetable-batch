package org.zenbot.szolnok.timetable.backend.service.bus

/**
 * Indicates that the batch job of the bus to remove is still in progress
 */
class BatchJobOfBusInProgressException(
    override val message: String
) : RuntimeException(message)
