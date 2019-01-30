package org.zenbot.szolnok.timetable.configuration.properties

data class TimetableResourceProperties(
    var baseUrl: String = "",
    var szolnokUrl: String = "",
    var selectedBuses: List<String> = ArrayList()
)