package org.zenbot.szolnok.timetable.backend.domain.api.stops

data class BusStopsResponse(
    val busName: String,
    val startStop: String,
    val endStop: String,
    val busStops: List<String>,
    val numberOfRoutes: Int,
    val found: Boolean
)
