package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class BatchJobEntityTransformer {
    fun transform(job: BatchJobEntity) =
            BatchJobResponse(
                    id = job.id,
                    type = job.type,
                    startTime = formatTime(job.startTime),
                    finishTime = formatTime(job.finishTime),
                    finished = job.finished,
                    status = job.status,
                    parameters = job.parameters.joinToString(separator = ",", prefix = "[",postfix = "]"),
                    promoted = job.promotedToProd,
                    promotable = job.promotable
        )

    fun formatTime(time: LocalDateTime?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val result: String
        if (time != null) {
            result = formatter.format(time)
        } else {
            result = ""
        }
        return result
    }
}
