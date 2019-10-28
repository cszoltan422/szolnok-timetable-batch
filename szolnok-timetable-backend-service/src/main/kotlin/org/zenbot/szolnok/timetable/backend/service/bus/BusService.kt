package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Service
@Transactional
class BusService(
    private val busRepository: BusRepository,
    private val busEntityTransformer: BusEntityTransformer
) {
    fun findAllByTargetState(query: String, targetState: TargetState): List<BusResponse> =
            busRepository.findAllByBusNameContainsAndTargetState(query, targetState)
                    .map { busEntity -> busEntityTransformer.transform(busEntity) }
}
