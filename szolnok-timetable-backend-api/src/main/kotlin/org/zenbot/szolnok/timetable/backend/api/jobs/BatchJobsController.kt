package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener

@RestController
class BatchJobsController(
    private val batchJobService: BatchJobService,
    private val jobLauncher: JobLauncher,
    private val batchJobMap: Map<String, Job>
) {

    @RequestMapping(
            value = arrayOf("/admin/api/jobs/recent"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.GET)
    )
    fun getRecentJobs(): List<BatchJobResponse> = batchJobService.findRecentJobs()

    @RequestMapping(
            value = arrayOf("/admin/api/jobs/launch"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf("application/json")
    )
    fun launchBatchJob(@RequestBody launchJobRequest: LaunchJobRequest): LauchBatchJobResponse {
        val job = batchJobMap.get(launchJobRequest.jobType)
        val response: LauchBatchJobResponse
        if (job != null) {
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
            response = LauchBatchJobResponse(success = true, message = "Started")
        } else {
            response = LauchBatchJobResponse(
                    success = false,
                    message = "Can't find job with name [" + launchJobRequest.jobType + "]"
            )
        }
        return response
    }
}