package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class BatchJobsController(
    private val batchJobService: BatchJobService,
    private val batchJobLauncherService: BatchJobLauncherService
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
        return batchJobLauncherService.launch(launchJobRequest)
    }
}