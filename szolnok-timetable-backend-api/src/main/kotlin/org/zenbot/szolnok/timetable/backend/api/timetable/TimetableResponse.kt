package org.zenbot.szolnok.timetable.backend.api.timetable

data class TimetableResponse(
    val busName: String,
    val startStop: String,
    val endStop: String,
    val busStopName: String,
    val occurrence: Int,
    var workDaySchedule: ScheduleResponse,
    var saturdaySchedule: ScheduleResponse,
    var sundaySchedule: ScheduleResponse,
    val found: Boolean

)
