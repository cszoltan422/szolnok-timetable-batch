package org.zenbot.szolnok.timetable.backend.service.jobs

import org.springframework.batch.core.BatchStatus
import org.springframework.stereotype.Service
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

/**
 * Service to validate no current batch job is running for a given type.
 */
@Service
class BatchJobValidatorService(
    private val batchJobRepository: BatchJobRepository
) {

    /**
     * Validates no current batch job is running for a given type.
     * @param launchJobRequest The request to start a new job
     * @throws BatchJobAlreadyRunningException if a batch job already running for the given type
     */
    fun validateJobNotRunning(launchJobRequest: LaunchJobRequest) {
        if (jobAlreadyRunning(launchJobRequest.jobType)) {
            throw BatchJobAlreadyRunningException("Batch job already running for type: [${launchJobRequest.jobType}]")
        }
    }

    private fun jobAlreadyRunning(jobType: String) =
            batchJobRepository.findAllByTypeAndStatusAndFinishedFalse(jobType, BatchStatus.STARTED)
                    .isNotEmpty()
}
