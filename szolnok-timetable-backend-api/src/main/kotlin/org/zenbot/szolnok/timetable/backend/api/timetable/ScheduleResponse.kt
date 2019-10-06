package org.zenbot.szolnok.timetable.backend.api.timetable

data class ScheduleResponse(
    val busArrivals: List<BusArrivalResponse>
)
