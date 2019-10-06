package org.zenbot.szolnok.timetable.backend.api.bus

data class BusResponse(
    val busName: String,
    val startBusStop: String,
    val endBusStop: String
)
