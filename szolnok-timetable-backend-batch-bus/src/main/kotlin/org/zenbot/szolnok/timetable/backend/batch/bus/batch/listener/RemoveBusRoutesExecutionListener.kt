package org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
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
        val routes = busRepository.findAll()
        if (!properties.resource.selectedBuses.isEmpty()) {
            log.info("Removing bus routes [{}]", properties.resource.selectedBuses.joinToString(","))
            routes.forEach { route ->
                if (properties.resource.selectedBuses.contains(route.busName)) {
                    route.busRoutes = ArrayList<BusRoute>()
                }
            }
        } else {
            log.info("Removing all bus routes from database")
            routes.forEach { route -> route.busRoutes = ArrayList<BusRoute>() }
        }
        busRepository.saveAll(routes)
    }

    override fun afterJob(jobExecution: JobExecution) {}
}
