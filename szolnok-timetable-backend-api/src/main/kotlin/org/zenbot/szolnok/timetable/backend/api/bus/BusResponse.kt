package org.zenbot.szolnok.timetable.backend.api.bus

data class BusResponse(
        var busName: String = "",
        var routes: MutableList<BusRouteRespose> = ArrayList()
)