package org.zenbot.szolnok.timetable.backend.service.cleanup

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Class to remove the buses marked for [TargetState.PURGATORY]
 */
@Service
class PurgatoryBusCleanupService(
    private val busRepository: BusRepository
) {

    private val log = LoggerFactory.getLogger(PurgatoryBusCleanupService::class.java)

    /**
     * Method to remove all the buses marked for [TargetState.PURGATORY]
     */
    fun cleanUpPurgatoryBuses() {
        log.info("Clearing all buses marked with [TargetState.PURGATORY]")
        busRepository.deleteAllByTargetState(TargetState.PURGATORY)
    }
}
