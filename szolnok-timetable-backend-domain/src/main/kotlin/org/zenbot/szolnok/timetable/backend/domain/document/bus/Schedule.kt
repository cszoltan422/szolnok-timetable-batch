package org.zenbot.szolnok.timetable.backend.domain.document.bus

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Schedule(
    @Id var id: String? = null,
    var busArrivals: MutableList<BusArrival> = ArrayList()
)
