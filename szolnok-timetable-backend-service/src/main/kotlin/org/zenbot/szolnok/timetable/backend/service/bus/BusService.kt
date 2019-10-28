package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

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
