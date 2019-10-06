package org.zenbot.szolnok.timetable.backend.api.stops

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity

@Component
class BusStopsTransformer {
    fun transform(bus: BusEntity, route: BusRouteEntity): BusStopsResponse =
            BusStopsResponse(
                    busName = bus.busName,
                    startStop = route.startBusStop,
                    endStop = route.endBusStop,
                    numberOfRoutes = bus.busRouteEntities.size,
                    busStops = route.busStopEntities.map { busStopEntity -> busStopEntity.busStopName },
                    found = true
            )

    fun empty() =
            BusStopsResponse(
                    busName = "",
                    startStop = "",
                    endStop = "",
                    numberOfRoutes = 0,
                    busStops = emptyList(),
                    found = false
            )
}
