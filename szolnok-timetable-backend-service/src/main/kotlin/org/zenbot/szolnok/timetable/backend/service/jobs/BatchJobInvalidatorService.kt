package org.zenbot.szolnok.timetable.backend.service.jobs

import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

@Service
class BatchJobInvalidatorService(
    private val batchJobRepository: BatchJobRepository
) {
    fun invalidatePreviousJobs(launchJobRequest: LaunchJobRequest) {
        batchJobRepository.findAllByTypeAndFinishedTrueAndPromotableTrue(
                launchJobRequest.jobType
        ).forEach {
            if (it.parameters == launchJobRequest.parameters) {
                it.promotable = false
                batchJobRepository.save(it)
            }
        }
    }
}
