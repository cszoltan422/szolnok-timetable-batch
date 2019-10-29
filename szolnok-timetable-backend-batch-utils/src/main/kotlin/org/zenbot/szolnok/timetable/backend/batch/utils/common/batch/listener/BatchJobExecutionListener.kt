package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import java.time.LocalDateTime

/**
 * Batch Job execution listener component that saves a new  [BatchJobEntity]
 */
@Component
@Transactional
class BatchJobExecutionListener(
    private val batchJobRepository: BatchJobRepository
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(BatchJobExecutionListener::class.java)

    /**
     * Marks the [BatchJobEntity] finished for the current jobId in the execution context.
     *  @param jobExecution The context of the currently executing job
     *
     */
    override fun afterJob(jobExecution: JobExecution) {
        val id = jobExecution.executionContext.getLong(BATCH_JOB_ENTITY_ID_KEY, DEFAULT_BATCH_JOB_ENTITY_ID_VALUE)
        val batchJob = batchJobRepository.findById(id)
        if (batchJob.isPresent) {
            val job = batchJob.get()
            job.finishTime = LocalDateTime.now()
            job.status = jobExecution.status
            job.finished = true

            log.info("Batch Job finished [" + job.id + "]")
            batchJobRepository.save(job)
        }
    }

    /**
     * Creates a new [BatchJobEntity], saves to the database and saves it's id to the current execution context
     *  @param jobExecution The context of the currently executing job
     *
     */
    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Save new Batch Job into database for name [" + jobExecution.jobInstance.jobName + "]")
        val selectedBuses = jobExecution
                .jobParameters
                .getString(SELECTED_BUSES_JOB_PARAMETER_KEY, DEFAULT_SELECTED_BUSES_JOB_PARAMETER_KEY_VALUE)
        val batchJob = BatchJobEntity()
        batchJob.startTime = LocalDateTime.now()
        batchJob.status = BatchStatus.STARTED
        batchJob.type = jobExecution.jobInstance.jobName
        batchJob.parameters = HashSet(selectedBuses.split(","))
        batchJob.finished = false
        batchJob.promotable = true

        val saved = batchJobRepository.saveAndFlush(batchJob)

        jobExecution.executionContext.putLong(BATCH_JOB_ENTITY_ID_KEY, saved.id ?: DEFAULT_BATCH_JOB_ENTITY_ID_VALUE)
    }

    companion object {
        val DEFAULT_BATCH_JOB_ENTITY_ID_VALUE = 0L
        val DEFAULT_SELECTED_BUSES_JOB_PARAMETER_KEY_VALUE = ""
        val BATCH_JOB_ENTITY_ID_KEY = "batchJobEntityId"
        val SELECTED_BUSES_JOB_PARAMETER_KEY = "selectedBuses"
        val SELECTED_BUSES_SPLIT_BY = ","
    }
}
