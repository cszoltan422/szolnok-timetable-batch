package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.writer

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

@Component
@StepScope
@Transactional
class BusRepositoryItemWriter(
    private val busRepository: BusRepository,
    private val batchJobRepository: BatchJobRepository
) : ItemWriter<BusEntity> {

    private val log = LoggerFactory.getLogger(BusRepositoryItemWriter::class.java)

    private lateinit var jobExecution: JobExecution

    @BeforeStep
    fun before(stepExecution: StepExecution) {
        this.jobExecution = stepExecution.jobExecution
    }

    override fun write(list: List<BusEntity>) {
        if (list.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [" + list.size + "]")
        }
        val bus = list[0]

        val batchJobId = jobExecution.executionContext.getLong(
                BatchJobExecutionListener.BATCH_JOB_ENTITY_ID_KEY,
                0L)
        val batchJob = batchJobRepository.findById(batchJobId)
        batchJob.ifPresent { bus.batchJobEntity = it }

        log.info("Saving bus=[#{}] to database", bus.busName)
        busRepository.save(bus)
    }
}
