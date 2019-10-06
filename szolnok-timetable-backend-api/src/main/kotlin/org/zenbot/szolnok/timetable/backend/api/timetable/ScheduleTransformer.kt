package org.zenbot.szolnok.timetable.backend.api.timetable

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

@Component
class ScheduleTransformer {
    fun transform(schedule: ScheduleEntity): ScheduleResponse {
        val busArrivals = schedule.busArrivalEntities.map { busArrivalEntity ->
            BusArrivalResponse(
                    arrivalHour = busArrivalEntity.arrivalHour,
                    arrivalMinute = busArrivalEntity.arrivalMinute ?: 0,
                    isLowfloor = busArrivalEntity.isLowfloor
            )
        }
        return ScheduleResponse(busArrivals)
    }
}
