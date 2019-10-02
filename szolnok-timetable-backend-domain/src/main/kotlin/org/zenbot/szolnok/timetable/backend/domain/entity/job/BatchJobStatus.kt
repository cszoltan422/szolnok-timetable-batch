package org.zenbot.szolnok.timetable.backend.domain.entity.job

enum class BatchJobStatus (val status: String) {
    UNDEFINED("undefined"),
    STARTED("started"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    FAILED("failed")
}