package org.zenbot.szolnok.timetable.backend.api.bus

data class BusResponse(
        val busName: String = "",
        val routes: List<BusRouteResponse> = listOf()
)
