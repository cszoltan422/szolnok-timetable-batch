package org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus
import org.zenbot.szolnok.timetable.backend.domain.document.bus.BusRoute
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Component
@EnableConfigurationProperties(TimetableProperties::class)
class RemoveBusRoutesExecutionListener(
        val busRepository: BusRepository,
        val properties: TimetableProperties
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        val buses = busRepository.findAll()

        val updatedBuses: List<Bus> =
                if (!properties.resource.selectedBuses.isEmpty()) {
                    log.info("Removing bus routes [{}]", properties.resource.selectedBuses.joinToString(","))
                    buses.map(this::removeRoutesIfSelected)
                } else {
                    log.info("Removing all bus routes from database")
                    buses.map(this::removeRoutes)
                }

        busRepository.saveAll(updatedBuses)
    }

    override fun afterJob(jobExecution: JobExecution) {}

    private fun removeRoutesIfSelected(bus: Bus): Bus =
            if (properties.resource.selectedBuses.contains(bus.busName)) {
                removeRoutes(bus)
            } else {
                bus
            }

    private fun removeRoutes(bus: Bus): Bus = bus.copy(busRoutes = mutableListOf<BusRoute>())

}
