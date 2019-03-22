package org.zenbot.szolnok.timetable.backend.domain.document.bus

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class BusArrival(
    @Id var id: String? = null,
    var isLowfloor: Boolean = false,
    var arrivalHour: Int = 0,
    var arrivalMinute: Int? = null
)