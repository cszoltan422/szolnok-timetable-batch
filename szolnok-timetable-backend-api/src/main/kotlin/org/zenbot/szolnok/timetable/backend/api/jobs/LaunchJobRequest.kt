package org.zenbot.szolnok.timetable.backend.api.jobs

data class LaunchJobRequest(
    val jobType: String,
    val parameters: String
)
