package org.zenbot.szolnok.timetable.backend.service.jobs

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LauchBatchJobResponse
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

/**
 * Class to launch a new batch job
 */
@Service
@Transactional
class BatchJobLauncherService(
    private val batchJobRepository: BatchJobRepository,
    private val jobLauncher: JobLauncher,
    private val batchJobMap: Map<String, Job>
) {

    /**
     * Launches a new job as per as the [LaunchJobRequest]
     * @param launchJobRequest The request to which job to launch
     * @return LauchBatchJobResponse if the the job was launchable
     * @throws NoSuchBatchJobException if there's no such job for the supplied type
     * @throws BatchJobAlreadyRunning if there's another job in progress for the given type
     */
    fun launch(launchJobRequest: LaunchJobRequest): LauchBatchJobResponse {
        val job = batchJobMap.get(launchJobRequest.jobType)
        val response: LauchBatchJobResponse
        if (job == null) {
            throw NoSuchBatchJobException("Can't find job with name [${launchJobRequest.jobType}]")
        } else if (jobAlreadyRunning(launchJobRequest)) {
            throw BatchJobAlreadyRunningException("Batch job already running for type: [${launchJobRequest.jobType}]")
        } else {
            invalidatePreviousJobs(launchJobRequest)
            launchJob(job, launchJobRequest)
            response = LauchBatchJobResponse(success = true, message = "Started")
        }
        return response
    }

    private fun invalidatePreviousJobs(launchJobRequest: LaunchJobRequest) {
        val paramsSet = HashSet(launchJobRequest.parameters.split(","))
        batchJobRepository.findAllByTypeAndFinishedTrueAndPromotableTrue(
                launchJobRequest.jobType
        ).forEach {
            if (it.parameters.equals(paramsSet)) {
                it.promotable = false
                batchJobRepository.saveAndFlush(it)
            }
        }
    }

    private fun jobAlreadyRunning(launchJobRequest: LaunchJobRequest) =
        batchJobRepository.findAllByTypeAndStatusAndFinishedFalse(launchJobRequest.jobType, BatchStatus.STARTED)
                .isNotEmpty()

    private fun launchJob(job: Job, launchJobRequest: LaunchJobRequest) {
        Thread(Runnable {
            jobLauncher.run(job, JobParametersBuilder()
                    .addString(BatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY, launchJobRequest.parameters)
                    .addLong("timestamp", System.currentTimeMillis()).toJobParameters()
            )
        }).start()
    }
}
