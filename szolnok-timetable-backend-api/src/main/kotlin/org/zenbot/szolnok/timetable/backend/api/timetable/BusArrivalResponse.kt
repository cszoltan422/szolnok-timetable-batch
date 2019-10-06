package org.zenbot.szolnok.timetable.backend.api.timetable

data class BusArrivalResponse(
    val isLowfloor: Boolean,
    val arrivalHour: Int,
    val arrivalMinute: Int
)
