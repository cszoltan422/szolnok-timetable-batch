package org.zenbot.szolnok.timetable.backend.batch.bus.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.SaveBatchJobExecutionListener
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
        val selectedBusesSplitted = selectedBuses.split(SaveBatchJobExecutionListener.SELECTED_BUSES_SPLIT_BY)
        val buses = busRepository.findAllByTargetState(TargetState.BATCH)
        log.info("Removing bus routes [{}]", selectedBuses)
        buses.forEach { bus ->
            if (shouldRemove(selectedBusesSplitted, bus)) {
                busRepository.delete(bus)
            }
        }
    }

    private fun shouldRemove(selectedBusesSplitted: List<String>, bus: BusEntity) =
            selectedBusesSplitted.contains(bus.busName)

    private fun hasSelectedBuses(selectedBuses: String) = selectedBuses.isNotEmpty()

    private fun getSelectedBusFromContext(jobExecution: JobExecution): String = jobExecution
            .jobParameters
            .getString(SaveBatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY,
                    SaveBatchJobExecutionListener.DEFAULT_SELECTED_BUSES_JOB_PARAMETER_KEY_VALUE)
}
