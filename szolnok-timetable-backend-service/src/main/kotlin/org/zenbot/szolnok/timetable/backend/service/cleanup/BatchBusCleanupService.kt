package org.zenbot.szolnok.timetable.backend.service.cleanup

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Removes the buses from the batch target state as per as the job parameters
 */
@Service
class BatchBusCleanupService(
    private val busRepository: BusRepository
) {

    private val log = LoggerFactory.getLogger(BatchBusCleanupService::class.java)

    /**
     * Removes the buses from the batch target state as per as the parameters of the launchJobRequest
     * @param launchJobRequest The request to start a new job
     */
    fun removePreviousBuses(launchJobRequest: LaunchJobRequest) {
        val selectedBuses = launchJobRequest.parameters
        if (hasSelectedBuses(selectedBuses)) {
            removeBusesOfList(selectedBuses)
        } else {
            removeAllBuses()
        }
    }

    private fun removeAllBuses() {
        log.info("Removing all buses from database")
        busRepository.deleteAllByTargetState(TargetState.BATCH)
    }

    private fun removeBusesOfList(selectedBuses: List<String>) {
        val buses = busRepository.findAllByTargetState(TargetState.BATCH)
        log.info("Removing bus [{}]", selectedBuses)
        buses.forEach { bus ->
            if (shouldRemove(selectedBuses, bus)) {
                busRepository.delete(bus)
            }
        }
    }

    private fun shouldRemove(parameters: List<String>, bus: BusEntity) =
            parameters.contains(bus.busName)

    private fun hasSelectedBuses(parameters: List<String>) = parameters.isNotEmpty()
}
