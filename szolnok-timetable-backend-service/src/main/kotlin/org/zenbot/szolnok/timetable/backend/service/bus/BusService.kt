package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Service class to operate on the [BusEntity] class
 */
@Service
@Transactional
class BusService(
    private val busRepository: BusRepository,
    private val busEntityTransformer: BusEntityTransformer
) {

    /**
     * Fetches all the buses matching the query and the given [TargetState]
     * @param query query string to match the busName
     * @param targetState The targetState to filter for
     * @return All the buses matching the query and in the given target state transformed into a [BusResponse]
     */
    fun findAllByTargetState(query: String, targetState: TargetState): List<BusResponse> =
            busRepository.findAllByBusNameContainsAndTargetStateAndBatchJobEntityFinishedTrue(query, targetState)
                    .map { busEntity -> busEntityTransformer.transform(busEntity) }

    /**
     * Removes a bus from the batch target state
     * @param busName The name of the bus
     * @throws BatchJobInProgressException If the batch process of the bus is still in progress
     * @throws BusNotFoundException If the bus is not found by the busName
     */
    fun removeBusFromBatchTargetState(busName: String) {
        val bus = busRepository.findByBusNameAndTargetState(busName, TargetState.BATCH)
        if (bus != null) {
            if (batchFinished(bus)) {
                busRepository.deleteByBusNameAndTargetState(busName, TargetState.BATCH)
            } else {
                throw BatchJobInProgressException("The batch process: [${bus.batchJobEntity?.id}]" +
                        "of the bus: [$busName] is still in progress!")
            }
        } else {
            throw BusNotFoundException("Bus[$busName] not found")
        }
    }

    private fun batchFinished(bus: BusEntity) =
            bus.batchJobEntity?.finished == true
}
