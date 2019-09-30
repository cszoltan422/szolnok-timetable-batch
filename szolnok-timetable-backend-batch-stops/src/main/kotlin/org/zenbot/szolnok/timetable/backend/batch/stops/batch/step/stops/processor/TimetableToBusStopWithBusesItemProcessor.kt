package org.zenbot.szolnok.timetable.backend.batch.stops.batch.step.stops.processor

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.stop.BusStopWithBusesEntity
import org.zenbot.szolnok.timetable.backend.repository.BusStopRepository

@Component
class TimetableToBusStopWithBusesItemProcessor(private val busStopRepository: BusStopRepository) : ItemProcessor<Timetable, BusStopWithBusesEntity> {

    private val log = LoggerFactory.getLogger(TimetableToBusStopWithBusesItemProcessor::class.java)

    override fun process(timetable: Timetable): BusStopWithBusesEntity {
        log.info("Fetching busStop=[${timetable.activeStopName}] from database")
        val busStopWithBuses = busStopRepository.findByBusStopName(timetable.activeStopName)
        val result: BusStopWithBusesEntity
        if (busStopWithBuses.isPresent) {
            result = busStopWithBuses.get()
            result.busStopName = timetable.activeStopName
            result.buses.add(timetable.busName)
        } else {
            result = BusStopWithBusesEntity()
            result.busStopName = timetable.activeStopName
            result.buses.add(timetable.busName)
        }
        return result
    }
}