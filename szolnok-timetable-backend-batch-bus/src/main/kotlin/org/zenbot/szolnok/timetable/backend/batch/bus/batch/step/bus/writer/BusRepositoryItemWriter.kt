package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.writer

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import javax.transaction.Transactional

@Component
@Transactional
class BusRepositoryItemWriter(
    private val busRepository: BusRepository
) : ItemWriter<BusEntity> {

    private val log = LoggerFactory.getLogger(BusRepositoryItemWriter::class.java)

    override fun write(list: List<BusEntity>) {
        if (list.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [" + list.size + "]")
        }

        val bus = list[0]
        log.info("Saving bus=[#{}, from={}, to={}] to database", bus.busName, bus.busRouteEntities[0].startBusStop, bus.busRouteEntities[0].endBusStop)
        busRepository.save(bus)
    }
}
