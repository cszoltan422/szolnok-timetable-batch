package org.zenbot.szolnok.timetable.backend.domain.api.timetable

data class ScheduleResponse(
    val busArrivals: List<BusArrivalResponse>
)
