package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Component
@Transactional
class RemoveBusRoutesExecutionListener(
    private val busRepository: BusRepository
) : JobExecutionListenerSupport() {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        val selectedBuses = jobExecution
                .jobParameters
                .getString(BatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY,
                        BatchJobExecutionListener.DEFAULT_SELECTED_BUSES_JOB_PARAMETER_KEY_VALUE)

        if (!selectedBuses.isEmpty()) {
            val selectedBusesSplitted = selectedBuses.split(BatchJobExecutionListener.SELECTED_BUSES_SPLIT_BY)
            val routes = busRepository.findAllByTargetState(TargetState.BATCH)
            log.info("Removing bus routes [{}]", selectedBuses)
            routes.forEach { route ->
                if (selectedBusesSplitted.contains(route.busName)) {
                    busRepository.delete(route)
                }
            }
        } else {
            log.info("Removing all bus routes from database")
            busRepository.deleteAllByTargetState(TargetState.BATCH)
        }
    }
}
