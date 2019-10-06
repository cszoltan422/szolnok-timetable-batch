package org.zenbot.szolnok.timetable.backend.api.bus

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

@Service
@Transactional
class BusService(
    private val busRepository: BusRepository,
    private val busEntityTransformer: BusEntityTransformer
) {
    fun findAll(query: String): List<BusResponse> =
            busRepository.findAllByBusNameContains(query)
                    .map { busEntity -> busEntityTransformer.transform(busEntity) }
}