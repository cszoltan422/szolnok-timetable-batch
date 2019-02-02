package org.zenbot.szolnok.timetable.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.zenbot.szolnok.timetable.configuration.properties.TimetableResourceProperties
import org.zenbot.szolnok.timetable.dao.BusRepository
import org.zenbot.szolnok.timetable.dao.BusStopRepository
import org.zenbot.szolnok.timetable.domain.BusRoute

class RemoveBusRoutesExecutionListener(
    val busRepository: BusRepository,
    val busStopRepository: BusStopRepository,
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
            busStopRepository.deleteAll()
        }
        busRepository.saveAll(routes)
    }

    override fun afterJob(jobExecution: JobExecution) {}
}
