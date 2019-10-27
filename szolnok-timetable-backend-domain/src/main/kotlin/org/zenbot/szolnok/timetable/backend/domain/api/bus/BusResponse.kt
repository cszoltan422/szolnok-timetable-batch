package org.zenbot.szolnok.timetable.backend.domain.api.bus

data class BusResponse(
    val busName: String,
    val startBusStop: String,
    val endBusStop: String,
    val batchJobId: Long
)
