package org.zenbot.szolnok.timetable.backend.domain.api.jobs

import org.springframework.batch.core.BatchStatus

data class BatchJobResponse(

    val id: Long?,
    val type: String,
    val startTime: String,
    val finishTime: String,
    val finished: Boolean,
    val status: BatchStatus,
    val parameters: String,
    val promoted: Boolean,
    val promotable: Boolean

)
