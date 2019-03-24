package org.zenbot.szolnok.timetable.backend.domain.document.bus

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Bus(
    @Id var id: String? = null,
    var busName: String = "",

    var busRoutes: MutableList<BusRoute> = ArrayList()
) {

    fun getBusRouteByStartStopName(startBusStopName: String): BusRoute = this.busRoutes.stream()
                .filter { line -> line.startBusStop == startBusStopName }
                .findFirst()
                .orElse(BusRoute())

    fun hasNoBusRoute(busRoute: BusRoute): Boolean = this.busRoutes.stream()
            .map { line -> line.startBusStop }
            .filter { startStop -> busRoute.startBusStop == startStop }
            .count() == 0L
}
