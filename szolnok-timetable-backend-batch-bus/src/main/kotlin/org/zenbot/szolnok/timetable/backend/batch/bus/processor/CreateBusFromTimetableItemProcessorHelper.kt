package org.zenbot.szolnok.timetable.backend.batch.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

/**
 * Fetches or creates a new [BusEntity] from the given [Timetable] object
 */
@Component
class CreateBusFromTimetableItemProcessorHelper(private val busRepository: BusRepository) {

    private val log = LoggerFactory.getLogger(CreateBusFromTimetableItemProcessorHelper::class.java)

    /**
     * Fetches or creates a new [BusEntity] from the given [Timetable] object
     * @param timetable The current timetable to process
     * @return A [BusEntity]. If not present in the database a new object will be created
     */
    fun createBusFromTimetable(timetable: Timetable): BusEntity {
        log.debug("Fetching bus=[#{}] from database", timetable.busName)
        val bus = busRepository.findByBusNameAndTargetState(timetable.busName, TargetState.BATCH)
        val result: BusEntity
        if (bus == null) {
            result = BusEntity(
                    busName = timetable.busName,
                    targetState = TargetState.BATCH
            )
        } else {
            result = bus
        }
        return result
    }
}
