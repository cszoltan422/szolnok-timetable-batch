package org.zenbot.szolnok.timetable.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Schedule(
    @Id var id: String = "",
    var busArrivals: List<BusArrival> = ArrayList()
)
