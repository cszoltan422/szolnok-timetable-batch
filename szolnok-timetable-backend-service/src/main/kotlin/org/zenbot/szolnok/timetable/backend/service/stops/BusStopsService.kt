package org.zenbot.szolnok.timetable.backend.service.stops

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.api.stops.BusStopsResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Returns the busStops of the supplied bus for the [TargetState.PRODUCTION]
 */
@Service
@Transactional
class BusStopsService(
    private val busRepository: BusRepository,
    private val busStopsTransformer: BusStopsTransformer
) {

    /**
     * Returns the busStops of the supplied bus for the [TargetState.PRODUCTION]
     * @param bus the name of the bus to fetch the busStops
     * @return [BusStopsResponse] for the given bus, empty if not found
     */
    fun findAllBusStopsOfBus(bus: String): BusStopsResponse {
        val findByBusName = busRepository.findByBusNameAndTargetState(bus, TargetState.PRODUCTION)
        val result: BusStopsResponse
        result = if (findByBusName != null) {
            busStopsTransformer.transform(findByBusName, findByBusName.busRouteEntities[0])
        } else {
            busStopsTransformer.empty()
        }
        return result
    }

    /**
     * Returns the busStops of the supplied bus for the [TargetState.PRODUCTION]
     * @param bus the name of the bus to fetch the busStops
     * @param startBusStop to mark which route to use
     * @return [BusStopsResponse] for the given bus, empty if not found
     */
    fun findAllBusStopsOfBus(bus: String, startBusStop: String): BusStopsResponse {
        val findByBusName = busRepository.findByBusNameAndTargetState(bus, TargetState.PRODUCTION)
        val result: BusStopsResponse
        result = if (findByBusName != null && !findByBusName.hasNoBusRoute(BusRouteEntity(startBusStop = startBusStop))) {
            busStopsTransformer.transform(findByBusName, findByBusName.getBusRouteByStartStopName(startBusStop))
        } else {
            busStopsTransformer.empty()
        }
        return result
    }
}
