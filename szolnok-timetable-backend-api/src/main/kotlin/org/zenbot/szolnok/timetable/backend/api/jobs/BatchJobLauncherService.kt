package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

@Service
@Transactional
class BatchJobLauncherService(
    private val batchJobRepository: BatchJobRepository,
    private val jobLauncher: JobLauncher,
    private val batchJobMap: Map<String, Job>
) {
    fun launch(launchJobRequest: LaunchJobRequest): LauchBatchJobResponse {
        val job = batchJobMap.get(launchJobRequest.jobType)
        val response: LauchBatchJobResponse
        if (job == null) {
            response = LauchBatchJobResponse(
                    success = false,
                    message = "Can't find job with name [" + launchJobRequest.jobType + "]"
            )
        } else if (jobAlreadyRunning(launchJobRequest)) {
            response = LauchBatchJobResponse(
                    success = false,
                    message = "Job already running!"
            )
        } else {
            val paramsSet = HashSet(launchJobRequest.parameters.split(","))
            batchJobRepository.findAllByTypeAndFinishedTrueAndPromotableTrue(
                    launchJobRequest.jobType
            ).forEach {
                if (it.parameters.equals(paramsSet)) {
                    it.promotable = false
                    batchJobRepository.saveAndFlush(it)
                }
            }
            launchJob(job, launchJobRequest)
            response = LauchBatchJobResponse(success = true, message = "Started")
        }
        return response
    }

    private fun jobAlreadyRunning(launchJobRequest: LaunchJobRequest) =
        batchJobRepository.findAllByTypeAndStatusAndFinishedFalse(launchJobRequest.jobType, BatchStatus.STARTED)
                .isNotEmpty()

    private fun launchJob(job: Job, launchJobRequest: LaunchJobRequest) {
        Thread(Runnable {
            jobLauncher.run(
                    job,
                    JobParametersBuilder()
                            .addString(
                                    BatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY,
                                    launchJobRequest.parameters
                            )
                            .addLong(
                                    "timestamp",
                                    System.currentTimeMillis()
                            )
                            .toJobParameters()
            )
        }).start()
    }
}