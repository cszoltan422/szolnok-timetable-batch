package org.zenbot.szolnok.timetable.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class BusArrival(
    @Id var id: String = "",
    var isLowfloor: Boolean = false,
    var arrivalHour: Int = 0,
    var arrivalMinute: Int? = null
)
