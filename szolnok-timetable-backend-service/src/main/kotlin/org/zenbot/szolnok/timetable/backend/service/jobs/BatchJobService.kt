package org.zenbot.szolnok.timetable.backend.service.jobs

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Service class to operate on the [BatchJobEntity] class
 */
@Service
@Transactional
class BatchJobService(
    private val busRepository: BusRepository,
    private val batchJobRepository: BatchJobRepository,
    private val batchJobEntityTransformer: BatchJobEntityTransformer
) {

    /**
     * Finds the 5 recent jobs order by their start time desc
     */
    fun findRecentJobs() =
            batchJobRepository.findTop5ByOrderByStartTimeDesc()
                    .map { job -> batchJobEntityTransformer.transform(job) }

    /**
     * Promotes a batch to production
     * @param id The id of the job to promote
     */
    fun promoteToProduction(id: Long) {
        val batchJob = batchJobRepository.findById(id)
        batchJob.ifPresent {
            if (!it.promotedToProd) {
                val promotableBuses = busRepository.findAllByBatchJobEntityAndTargetState(it, TargetState.BATCH)
                val currentProductionBuses = busRepository.findAllByBusNameInAndTargetState(
                        promotableBuses.map { it.busName }.toSet(),
                        TargetState.PRODUCTION
                )
                changeTargetStateOfBuses(currentProductionBuses, TargetState.PURGATORY)
                changeTargetStateOfBuses(promotableBuses, TargetState.PRODUCTION)
                it.promotedToProd = true
                batchJobRepository.save(it)
            }
        }
    }

    private fun changeTargetStateOfBuses(
        buses: List<BusEntity>,
        futureState: TargetState
    ) {
        buses.forEach {
            it.targetState = futureState
            busRepository.save(it)
        }
    }
}
