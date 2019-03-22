package org.zenbot.szolnok.timetable.backend.domain.document.bus

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class BusStop(
    @Id var id: String? = null,
    var busStopName: String = "",
    var workDaySchedule: Schedule = Schedule(),
    var saturdaySchedule: Schedule = Schedule(),
    var sundaySchedule: Schedule = Schedule()
)