package org.zenbot.szolnok.timetable.backend.api.bus

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus

@Component
class BusTransformer {

    fun transform(buses: List<Bus>): List<BusResponse> {
        return buses.map(this::transformOne)
    }

    private fun transformOne(bus: Bus): BusResponse {
        val busRoutes = bus.busRoutes.map { BusRouteResponse(it.startBusStop, it.endBusStop) }
        return BusResponse(bus.busName, busRoutes)
    }
}
