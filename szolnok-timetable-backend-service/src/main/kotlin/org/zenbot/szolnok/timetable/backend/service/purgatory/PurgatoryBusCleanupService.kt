package org.zenbot.szolnok.timetable.backend.service.purgatory

import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Aspect class to remove the buses marked for [TargetState.PURGATORY]
 */
@Aspect
@Service
class PurgatoryBusCleanupService(
    private val busRepository: BusRepository
) {

    private val log = LoggerFactory.getLogger(PurgatoryBusCleanupService::class.java)

    /**
     * After aspect to remove all the buses marked for [TargetState.PURGATORY] after the [BatchJobService::promoteToProduction]
     * finished it's execution
     */
    @After("promoteToProduction()")
    fun cleanUpPurgatoryBuses() {
        log.info("Clearing all buses marked with [TargetState.PURGATORY]")
        busRepository.deleteAllByTargetState(TargetState.PURGATORY)
    }

    @Pointcut("execution (* " +
            "org.zenbot.szolnok.timetable.backend.service.jobs.BatchJobService.promoteToProduction(..)" +
            ")")
    private fun promoteToProduction() {
    }
}
