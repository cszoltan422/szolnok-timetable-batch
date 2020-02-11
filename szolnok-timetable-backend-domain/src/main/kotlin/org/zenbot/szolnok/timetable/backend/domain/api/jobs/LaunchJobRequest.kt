package org.zenbot.szolnok.timetable.backend.domain.api.jobs

data class LaunchJobRequest(
    val jobType: String,
    val parameters: List<String>
)
