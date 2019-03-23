package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.writer

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRouteRepository

@Component
class BusMongoRepositoryItemWriter(
    private val busRepository: BusRepository,
    private val busRouteRepository: BusRouteRepository
) : ItemWriter<Bus> {

    private val log = LoggerFactory.getLogger(BusMongoRepositoryItemWriter::class.java)

    override fun write(list: List<Bus>) {
        if (list.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [" + list.size + "]")
        }

        val bus = list[0]
        log.info("Saving bus=[#{}, from={}, to={}] to database", bus.busName, bus.busRoutes[0].startBusStop, bus.busRoutes[0].endBusStop)
        busRouteRepository.saveAll(bus.busRoutes)
        busRepository.save(bus)
    }
}
