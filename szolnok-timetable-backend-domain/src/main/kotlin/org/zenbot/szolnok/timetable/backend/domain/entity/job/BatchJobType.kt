package org.zenbot.szolnok.timetable.backend.domain.entity.job

enum class BatchJobType(val type: String) {
    UNDEFINED("undefined"),
    BUSES("buses"),
    BUS_STOPS("bus_stops"),
    NEWS("news")
}