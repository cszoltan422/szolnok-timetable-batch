package org.zenbot.szolnok.timetable.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Bus(
    @Id var id: String = "",
    var busName: String = "",
    var busRoutes: MutableList<BusRoute> = ArrayList()
) {

    fun getBusRouteByStartStopName(startBusStopName: String): BusRoute {
        val routeLine = this.busRoutes.stream()
                .filter { line -> line.startBusStop == startBusStopName }
                .findFirst()
        if (routeLine.isPresent) {
            return routeLine.get()
        } else {
            val result = BusRoute()
            result.busStops = ArrayList()
            return result
        }
    }

    fun hasNoBusRoute(busRoute: BusRoute): Boolean {
        return this.busRoutes.stream()
                .map { line -> line.startBusStop }
                .filter { startStop -> busRoute.startBusStop == startStop }
                .count() == 0L
    }
}
