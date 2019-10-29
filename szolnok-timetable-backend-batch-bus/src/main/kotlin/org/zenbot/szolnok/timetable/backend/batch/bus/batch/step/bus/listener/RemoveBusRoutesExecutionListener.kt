package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Batch job execution listener component. Removes the buses from the batch target state as per as the job parameters
 */
@Component
@Transactional
class RemoveBusRoutesExecutionListener(
    private val busRepository: BusRepository
) : JobExecutionListenerSupport() {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    /**
     * Removes the buses from the batch target state as per as the selectedBuses job parameter in the current [jobExecution]
     * @param jobExecution The context of the current job execution
     */
    override fun beforeJob(jobExecution: JobExecution) {
        val selectedBuses = getSelectedBusFromContext(jobExecution)
        if (hasSelectedBuses(selectedBuses)) {
            removeBusesOfList(selectedBuses)
        } else {
            removeAllBuses()
        }
    }

    private fun removeAllBuses() {
        log.info("Removing all bus routes from database")
        busRepository.deleteAllByTargetState(TargetState.BATCH)
    }

    private fun removeBusesOfList(selectedBuses: String) {
        val selectedBusesSplitted = selectedBuses.split(BatchJobExecutionListener.SELECTED_BUSES_SPLIT_BY)
        val routes = busRepository.findAllByTargetState(TargetState.BATCH)
        log.info("Removing bus routes [{}]", selectedBuses)
        routes.forEach { route ->
            if (shouldRemove(selectedBusesSplitted, route)) {
                busRepository.delete(route)
            }
        }
    }

    private fun shouldRemove(selectedBusesSplitted: List<String>, route: BusEntity) =
            selectedBusesSplitted.contains(route.busName)

    private fun hasSelectedBuses(selectedBuses: String) = !selectedBuses.isEmpty()

    private fun getSelectedBusFromContext(jobExecution: JobExecution): String {
        val selectedBuses = jobExecution
                .jobParameters
                .getString(BatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY,
                        BatchJobExecutionListener.DEFAULT_SELECTED_BUSES_JOB_PARAMETER_KEY_VALUE)
        return selectedBuses
    }
}
