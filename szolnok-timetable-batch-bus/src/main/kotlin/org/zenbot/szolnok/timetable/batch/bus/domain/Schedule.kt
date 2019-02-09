package org.zenbot.szolnok.timetable.batch.bus.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Schedule(
    @Id var id: String? = null,
    var busArrivals: MutableList<BusArrival> = ArrayList()
)
