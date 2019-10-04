package org.zenbot.szolnok.timetable.backend.api.stops

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

@Service
@Transactional
class BusStopsService(
        private val busRepository: BusRepository,
        private val busStopsTransformer: BusStopsTransformer
) {
    fun findAllBusStopsOfBus(bus: String): BusStopsResponse {
        val findByBusName = busRepository.findByBusName(bus)
        val result: BusStopsResponse
        result = if (findByBusName != null) {
            busStopsTransformer.transform(findByBusName, findByBusName.busRouteEntities[0])
        } else {
            busStopsTransformer.empty()
        }
        return result
    }

    fun findAllBusStopsOfBus(bus: String, startBusStop: String): BusStopsResponse {
        val findByBusName = busRepository.findByBusName(bus)
        val result: BusStopsResponse
        result = if (findByBusName != null && !findByBusName.hasNoBusRoute(BusRouteEntity(startBusStop = startBusStop))) {
            busStopsTransformer.transform(findByBusName, findByBusName.getBusRouteByStartStopName(startBusStop))
        } else {
            busStopsTransformer.empty()
        }
        return result
    }

}
