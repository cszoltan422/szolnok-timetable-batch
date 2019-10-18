package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

@Component
@Transactional
@EnableConfigurationProperties(TimetableProperties::class)
class RemoveBusRoutesExecutionListener(
    private val busRepository: BusRepository,
    private val properties: TimetableProperties
) : JobExecutionListenerSupport() {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        if (!properties.resource.selectedBuses.isEmpty()) {
            val routes = busRepository.findAll()
            log.info("Removing bus routes [{}]", properties.resource.selectedBuses.joinToString(","))
            routes.forEach { route ->
                if (properties.resource.selectedBuses.contains(route.busName)) {
                    busRepository.delete(route)
                }
            }
        } else {
            log.info("Removing all bus routes from database")
            busRepository.deleteAll()
        }
    }
}
