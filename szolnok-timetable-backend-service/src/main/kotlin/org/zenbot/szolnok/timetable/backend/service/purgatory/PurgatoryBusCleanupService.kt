package org.zenbot.szolnok.timetable.backend.service.purgatory

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Scheduled class to remove the buses marked for [TargetState.PURGATORY]
 */
@Service
@Transactional
class PurgatoryBusCleanupService(
    private val busRepository: BusRepository
) {

    private val log = LoggerFactory.getLogger(PurgatoryBusCleanupService::class.java)

    /**
     * Scheduled task to remove all the buses marked for [TargetState.PURGATORY] in every
     * finished it's execution
     */
    @Scheduled(fixedDelay = 60000)
    fun cleanUpPurgatoryBuses() {
        log.info("Clearing all buses marked with [TargetState.PURGATORY]")
        busRepository.deleteAllByTargetState(TargetState.PURGATORY)
    }
}
