package org.zenbot.szolnok.timetable.backend.service.timetable

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.BusArrivalResponse
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.ScheduleResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

/**
 * Transforms a [ScheduleEntity] into a [ScheduleResponse]
 */
@Component
class ScheduleTransformer {

    /**
     * Transforms a [ScheduleEntity] into a [ScheduleResponse]
     * @param schedule The [ScheduleEntity] to use in the transformation
     * @return [ScheduleResponse] transformed from the schedule
     */
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
