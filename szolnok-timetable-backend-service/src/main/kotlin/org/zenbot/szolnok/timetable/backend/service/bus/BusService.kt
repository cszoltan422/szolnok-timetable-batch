package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Service
@Transactional
class BusService(
    private val busRepository: BusRepository,
    private val busEntityTransformer: BusEntityTransformer
) {
    fun findAllByTargetState(query: String, targetState: TargetState): List<BusResponse> =
            busRepository.findAllByBusNameContainsAndTargetStateAndBatchJobEntityFinishedTrue(query, targetState)
                    .map { busEntity -> busEntityTransformer.transform(busEntity) }

    fun removeBusFromBatchTargetState(busName: String) {
        val bus = busRepository.findByBusNameAndTargetState(busName, TargetState.BATCH)
        if (bus != null) {
            if (batchInProgress(bus)) {
                busRepository.deleteByBusNameAndTargetState(busName, TargetState.BATCH)
            } else {
                throw BatchJobInProgressException("The batch process: [${bus.batchJobEntity?.id}]" +
                        "of the bus: [$busName] is still in progress!")
            }
        } else {
            throw BusNotFoundException("Bus[$busName] not found")
        }
    }

    private fun batchInProgress(bus: BusEntity) =
            bus.batchJobEntity?.finished == true
}
