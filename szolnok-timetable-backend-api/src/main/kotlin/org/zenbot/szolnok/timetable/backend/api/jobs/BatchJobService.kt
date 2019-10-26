package org.zenbot.szolnok.timetable.backend.api.jobs

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Service
@Transactional
class BatchJobService(
    private val busRepository: BusRepository,
    private val batchJobRepository: BatchJobRepository,
    private val batchJobEntityTransformer: BatchJobEntityTransformer
) {
    fun findRecentJobs() =
            batchJobRepository.findTop5ByOrderByStartTimeDesc()
                    .map { job -> batchJobEntityTransformer.transform(job) }

    fun promoteToProduction(id: Long) {
        val batchJob = batchJobRepository.findById(id)
        batchJob.ifPresent {
            if (!it.promotedToProd) {
                changeTargetStateOfBuses(it, TargetState.PRODUCTION, TargetState.PURGATORY)
                changeTargetStateOfBuses(it, TargetState.BATCH, TargetState.PRODUCTION)
                it.promotedToProd = true
                batchJobRepository.saveAndFlush(it)
            }
        }
    }

    private fun changeTargetStateOfBuses(
        batchJobEntity: BatchJobEntity,
        currentState: TargetState,
        futureState: TargetState
    ) {
        busRepository.findAllByBatchJobEntityAndTargetState(batchJobEntity, currentState)
                .forEach {
                    it.targetState = futureState
                    busRepository.saveAndFlush(it)
                }
    }
}
