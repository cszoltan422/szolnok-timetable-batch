package org.zenbot.szolnok.timetable.backend.api.bus

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Service
class BusService(private val busRepository: BusRepository,
                 private val busTransformer: BusTransformer) {

    fun getBuses(query: String?): List<BusResponse> {
        val buses = if (query != null) {
            busRepository.findAllByBusNameLikeOrderByBusName(query)
        } else {
            busRepository.findBusesByBusNameNotNull()
        }
        return busTransformer.transform(buses)
    }

}