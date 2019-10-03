package org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobStatus
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import java.time.LocalDateTime

@Component
@Transactional
@EnableConfigurationProperties(TimetableProperties::class)
class BatchJobExecutionListener(
    private val batchJobRepository: BatchJobRepository,
    val properties: TimetableProperties
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        val id = jobExecution.executionContext.getLong("batchJobEntityId")
        val batchJob = batchJobRepository.findById(id)
        if (batchJob.isPresent) {
            val job = batchJob.get()
            job.finishTime = LocalDateTime.now()
            job.status = BatchJobStatus.FINISHED
            job.finished = true

            log.info("Batch Job finished [" + job.id + "]")
            batchJobRepository.save(job)
        }
    }

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Save new Batch Job into database for name [" + jobExecution.jobInstance.jobName + "]")

        val batchJob = BatchJobEntity()
        batchJob.startTime = LocalDateTime.now()
        batchJob.status = BatchJobStatus.STARTED
        batchJob.type = jobExecution.jobInstance.jobName
        batchJob.parameters = properties.resource.selectedBuses.joinToString(prefix = "[", postfix = "]")
        batchJob.finished = false

        val saved = batchJobRepository.saveAndFlush(batchJob)

        jobExecution.executionContext.putLong("batchJobEntityId", saved.id ?: 0)
    }
}