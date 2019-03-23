package org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRouteRepository

@Component
class RemoveBusRoutesExecutionListener(
    private val busRepository: BusRepository,
    private val busRouteRepository: BusRouteRepository
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(RemoveBusRoutesExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        busRouteRepository.deleteAll()
        busRepository.deleteAll()
    }

    override fun afterJob(jobExecution: JobExecution) {}
}
