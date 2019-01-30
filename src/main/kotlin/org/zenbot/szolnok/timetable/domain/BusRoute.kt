package org.zenbot.szolnok.timetable.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class BusRoute(
    @Id var id: String = "",
    var startBusStop: String = "",
    var endBusStop: String = "",
    var busStops: MutableList<BusStop> = ArrayList()
) {

    fun addBusStopTimetable(busStop: BusStop) {
        busStops.add(busStop)
    }
}
