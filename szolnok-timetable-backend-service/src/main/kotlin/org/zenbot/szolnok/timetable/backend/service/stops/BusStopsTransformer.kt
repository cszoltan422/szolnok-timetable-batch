package org.zenbot.szolnok.timetable.backend.service.stops

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.api.stops.BusStopsResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity

/**
 * Transforms a [BusRouteEntity] into a [BusStopsResponse]
 */
@Component
class BusStopsTransformer {

    /**
     * Transforms a [BusRouteEntity] into a [BusStopsResponse]
     * @param bus The bus to use in the transformation
     * @param route The route to use in the transformation
     * @return [BusStopsResponse] transformed from the bus and route
     */
    fun transform(bus: BusEntity, route: BusRouteEntity): BusStopsResponse =
            BusStopsResponse(
                    busName = bus.busName,
                    startStop = route.startBusStop,
                    endStop = route.endBusStop,
                    numberOfRoutes = bus.busRouteEntities.size,
                    busStops = route.busStopEntities.map { busStopEntity -> busStopEntity.busStopName },
                    found = true
            )

    /**
     * Creates an empty [BusStopsResponse]
     * @return an empty [BusStopsResponse]
     */
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
