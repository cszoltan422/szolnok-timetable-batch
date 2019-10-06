package org.zenbot.szolnok.timetable.backend.api.timetable

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.api.stops.BusStopsResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

@Service
@Transactional
class TimetableService(
        private val busRepository: BusRepository,
        private val timetableTransformer : TimetableTransformer
) {

    fun getTimetable(bus: String, startBusStop: String, busStopName: String, occurrence: Int): TimetableResponse {
        val findByBusName = busRepository.findByBusName(bus)
        var result: TimetableResponse = timetableTransformer.empty()
        if (findByBusName != null && !findByBusName.hasNoBusRoute(BusRouteEntity(startBusStop = startBusStop))) {
            val busRouteByStartStopName = findByBusName.getBusRouteByStartStopName(startBusStop)
            val busStops = busRouteByStartStopName.busStopEntities
            var foundOccurrences = 0
            for (busStop in busStops) {
                if (busStop.busStopName.equals(busStopName)) {
                    foundOccurrences++

                    if (foundOccurrences == occurrence) {
                        result = timetableTransformer.transform(findByBusName, busRouteByStartStopName, busStop, occurrence)
                    }
                }
            }
        }
        return result
    }

}
