package org.zenbot.szolnok.timetable.backend.batch.utils.common.properties

data class TimetableResourceProperties(
    var baseUrl: String = "",
    var szolnokUrl: String = "",
    var selectedBuses: List<String> = ArrayList()
)