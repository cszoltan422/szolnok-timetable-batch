package org.zenbot.szolnok.timetable.backend.service.bus

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity

@Component
class BusEntityTransformer {
    fun transform(busEntity: BusEntity): BusResponse =
            BusResponse(
                    busName = busEntity.busName,
                    startBusStop = busEntity.busRouteEntities[0].startBusStop,
                    endBusStop = busEntity.busRouteEntities[0].endBusStop,
                    batchJobId = busEntity.batchJobEntity?.id ?: 0L
            )
}
