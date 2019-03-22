package org.zenbot.szolnok.timetable.backend.batch.stops.batch.step.stops.writer

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.stops.dao.BusStopRepository
import org.zenbot.szolnok.timetable.backend.batch.stops.domain.BusStopWithBuses

@Component
class StopsWithBusesMongoItemWriter(private val busStopRepository: BusStopRepository) : ItemWriter<BusStopWithBuses> {

    private val log = LoggerFactory.getLogger(StopsWithBusesMongoItemWriter::class.java)

    override fun write(list: List<BusStopWithBuses>) {
        if (list.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [" + list.size + "]")
        }

        val busStop = list[0]
        log.info("Saving busStop=[{}] with buses=[{}] to database", busStop.busStopName, busStop.buses)
        busStopRepository.save(busStop)
    }
}