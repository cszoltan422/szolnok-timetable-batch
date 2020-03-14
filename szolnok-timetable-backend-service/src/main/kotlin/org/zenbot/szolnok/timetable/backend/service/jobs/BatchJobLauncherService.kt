package org.zenbot.szolnok.timetable.backend.service.jobs

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.SaveBatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LauchBatchJobResponse
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.service.cleanup.BatchBusCleanupService

/**
 * Class to launch a new batch job
 */
@Service
@Transactional
class BatchJobLauncherService(
    private val batchBusCleanupService: BatchBusCleanupService,
    private val batchJobValidatorService: BatchJobValidatorService,
    private val batchJobInvalidatorService: BatchJobInvalidatorService,
    private val jobLauncher: JobLauncher,
    private val batchJobMap: Map<String, Job>
) {

    /**
     * Launches a new job as per as the [LaunchJobRequest]
     * @param launchJobRequest The request to which job to launch
     * @return LauchBatchJobResponse if the the job was launchable
     * @throws NoSuchBatchJobException if there's no such job for the supplied type
     * @throws EmptyJobParametersException if there's no parameter supplied for the given job
     * @throws BatchJobAlreadyRunning if there's another job in progress for the given type
     */
    fun launch(launchJobRequest: LaunchJobRequest): LauchBatchJobResponse {
        val result: LauchBatchJobResponse
        val job = batchJobMap[launchJobRequest.jobType]
        if (job != null) {
            batchJobValidatorService.validateNotEmptyParameterList(launchJobRequest)
            batchJobValidatorService.validateJobNotRunning(launchJobRequest)
            batchJobInvalidatorService.invalidatePreviousJobs(launchJobRequest)
            batchBusCleanupService.removePreviousBuses(launchJobRequest)
            launchJob(job, launchJobRequest)
            result = LauchBatchJobResponse(success = true, message = "Start")
        } else {
            throw NoSuchBatchJobException("Can't find job with name [${launchJobRequest.jobType}]")
        }
        return result
    }

    private fun launchJob(job: Job, launchJobRequest: LaunchJobRequest) {
        Thread(Runnable {
            jobLauncher.run(job, JobParametersBuilder()
                    .addString(
                            SaveBatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY,
                            launchJobRequest.parameters.joinToString(separator = SaveBatchJobExecutionListener.SELECTED_BUSES_SPLIT_BY))
                    .addLong("timestamp", System.currentTimeMillis()).toJobParameters()
            )
        }).start()
    }
}
