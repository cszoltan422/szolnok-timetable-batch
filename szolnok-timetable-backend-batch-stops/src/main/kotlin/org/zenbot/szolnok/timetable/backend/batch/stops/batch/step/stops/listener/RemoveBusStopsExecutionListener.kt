package org.zenbot.szolnok.timetable.backend.batch.stops.batch.step.stops.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.stops.dao.BusStopRepository

@Component
class RemoveBusStopsExecutionListener(private val busStopRepository: BusStopRepository) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(RemoveBusStopsExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Remove all busStops from database")
        busStopRepository.deleteAll()
    }

    override fun afterJob(jobExecution: JobExecution) {}
}