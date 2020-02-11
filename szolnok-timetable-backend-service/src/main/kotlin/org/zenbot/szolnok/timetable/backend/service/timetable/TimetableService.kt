package org.zenbot.szolnok.timetable.backend.service.timetable

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.TimetableResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

/**
 * Service class to return the timetable for a given bus, route and busStop with the given occurrence
 */
@Service
@Transactional
class TimetableService(
    private val busRepository: BusRepository,
    private val timetableTransformer: TimetableTransformer
) {

    /**
     * Returns the timetable for a given bus, route and busStop with the given occurrence
     * @param bus The name of the bus
     * @param startBusStop The startBusStop to pick the busRoute
     * @param busStopName The name of the busStop to fetch the timetable
     * @param occurrence Marks which occurrence of the busStop to fetch
     */
    fun getTimetable(bus: String, startBusStop: String, busStopName: String, occurrence: Int): TimetableResponse {
        val findByBusName = busRepository.findByBusNameAndTargetState(bus, TargetState.PRODUCTION)
        var result = timetableTransformer.empty()
        if (findByBusName != null && busIsPresentAndHasRoute(findByBusName, startBusStop)) {
            val busRouteByStartStopName = findByBusName.getBusRouteByStartStopName(startBusStop)
            val busStops = busRouteByStartStopName.busStopEntities
            result = getResultForBusStopAndOccurrence(
                    busStops,
                    busStopName,
                    occurrence,
                    findByBusName,
                    busRouteByStartStopName
            )
        }
        return result
    }

    private fun getResultForBusStopAndOccurrence(
        busStops: MutableList<BusStopEntity>,
        busStopName: String,
        occurrence: Int,
        findByBusName: BusEntity,
        busRouteByStartStopName: BusRouteEntity
    ): TimetableResponse {
        var foundOccurrences = 0
        var result = timetableTransformer.empty()
        for (busStop in busStops) {
            if (busStop.busStopName == busStopName) {
                foundOccurrences++
                if (foundOccurrences == occurrence) {
                    result = timetableTransformer.transform(findByBusName, busRouteByStartStopName, busStop, occurrence)
                }
            }
        }
        return result
    }

    private fun busIsPresentAndHasRoute(findByBusName: BusEntity, startBusStop: String) =
            !findByBusName.hasNoBusRoute(BusRouteEntity(startBusStop = startBusStop))
}
