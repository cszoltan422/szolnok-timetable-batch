package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity

/**
 * Class to transform a [BusEntity] into a [BusResponse]
 */
@Component
class BusEntityTransformer {

    /**
     * Transforms a [BusEntity] into a [BusResponse]
     * @param busEntity The [BusEntity] to transform
     * @return a [BusResponse] created from the entity
     */
    fun transform(busEntity: BusEntity): BusResponse =
            BusResponse(
                    busName = busEntity.busName,
                    startBusStop = busEntity.busRouteEntities[0].startBusStop,
                    endBusStop = busEntity.busRouteEntities[0].endBusStop,
                    batchJobId = busEntity.batchJobEntity?.id ?: 0L
            )
}
