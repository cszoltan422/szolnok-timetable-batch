package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

@Service
class BatchJobService(
    private val batchJobRepository: BatchJobRepository,
    private val batchJobEntityTransformer: BatchJobEntityTransformer
) {
    fun findRecentJobs() =
            batchJobRepository.findTop10ByOrderByStartTimeDesc()
                    .map { job -> batchJobEntityTransformer.transform(job) }
}
