package org.zenbot.szolnok.timetable.backend.batch.stops.batch.step.stops.processor

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.stops.dao.BusStopRepository
import org.zenbot.szolnok.timetable.backend.domain.document.stop.BusStopWithBuses
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable

@Component
class TimetableToBusStopWithBusesItemProcessor(private val busStopRepository: BusStopRepository) : ItemProcessor<Timetable, BusStopWithBuses> {

    private val log = LoggerFactory.getLogger(TimetableToBusStopWithBusesItemProcessor::class.java)

    override fun process(timetable: Timetable): BusStopWithBuses {
        log.info("Fetching busStop=[${timetable.activeStopName}] from database")
        val busStopWithBuses = busStopRepository.findByBusStopName(timetable.activeStopName)
        val result: BusStopWithBuses
        if (busStopWithBuses.isPresent) {
            result = busStopWithBuses.get()
            result.busStopName = timetable.activeStopName
            result.buses.add(timetable.busName)
        } else {
            result = BusStopWithBuses()
            result.busStopName = timetable.activeStopName
            result.buses.add(timetable.busName)
        }
        return result
    }
}