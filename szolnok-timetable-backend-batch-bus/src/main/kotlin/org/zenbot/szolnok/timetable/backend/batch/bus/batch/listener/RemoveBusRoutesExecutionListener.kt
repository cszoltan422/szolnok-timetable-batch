package org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.bus.dao.BusRepository
import org.zenbot.szolnok.timetable.backend.domain.document.bus.BusRoute
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableResourceProperties

class RemoveBusRoutesExecutionListener(
    val busRepository: BusRepository,
    val properties: TimetableResourceProperties
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        val routes = busRepository.findAll()
        if (!properties.selectedBuses.isEmpty()) {
            log.info("Removing bus routes [{}]", properties.selectedBuses.joinToString(","))
            routes.forEach { route ->
                if (properties.selectedBuses.contains(route.busName)) {
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
