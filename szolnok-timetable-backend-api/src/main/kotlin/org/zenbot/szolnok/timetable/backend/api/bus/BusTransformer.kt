package org.zenbot.szolnok.timetable.backend.api.bus

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus

@Component
class BusTransformer {

    fun transform(buses: List<Bus>): List<BusResponse> {
        val result : MutableList<BusResponse> = ArrayList()
        buses.forEach{it -> result.add(transformOne(it))}
        return result
    }

    private fun transformOne(bus: Bus): BusResponse {
        val result = BusResponse()
        result.busName = bus.busName
        val busRoutes : MutableList<BusRouteRespose> = ArrayList()
        bus.busRoutes.forEach{ it ->
            run {
                val busRouteRespose = BusRouteRespose()
                busRouteRespose.startStopName = it.startBusStop
                busRouteRespose.endStopName = it.endBusStop
                busRoutes.add(busRouteRespose)
            }
        }
        result.routes = busRoutes
        return result
    }
}