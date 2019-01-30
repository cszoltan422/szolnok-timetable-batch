package org.zenbot.szolnok.timetable.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.dao.BusRepository
import org.zenbot.szolnok.timetable.domain.Bus
import org.zenbot.szolnok.timetable.domain.Timetable

@Component
class CreateBusFromTimetableItemProcessorHelper(private val busRepository: BusRepository) {

    private val log = LoggerFactory.getLogger(CreateBusFromTimetableItemProcessorHelper::class.java)

    fun createBusFromTimetable(timetable: Timetable): Bus {
        log.debug("Fetching bus=[#{}] from database", timetable.busName)
        val bus = busRepository.findByBusName(timetable.busName)
        bus.busName = timetable.busName
        return bus
    }
}
