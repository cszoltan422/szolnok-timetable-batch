package org.zenbot.szolnok.timetable.backend.api.timetable

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity

@Component
class TimetableTransformer(
    private val scheduleTransformer: ScheduleTransformer
) {
    fun empty() =
            TimetableResponse(
                    busName = "",
                    startStop = "",
                    endStop = "",
                    busStopName = "",
                    occurrence = 0,
                    workDaySchedule = ScheduleResponse(
                            emptyList()
                    ),
                    saturdaySchedule = ScheduleResponse(
                            emptyList()
                    ),
                    sundaySchedule = ScheduleResponse(
                            emptyList()
                    ),
                    found = false
            )

    fun transform(bus: BusEntity, busRoute: BusRouteEntity, busStop: BusStopEntity, occurrence: Int): TimetableResponse {
        return TimetableResponse(
                busName = bus.busName,
                startStop = busRoute.startBusStop,
                endStop = busRoute.endBusStop,
                busStopName = busStop.busStopName,
                occurrence = occurrence,
                workDaySchedule = scheduleTransformer.transform(busStop.workDayScheduleEntity),
                saturdaySchedule = scheduleTransformer.transform(busStop.saturdayScheduleEntity),
                sundaySchedule = scheduleTransformer.transform(busStop.sundayScheduleEntity),
                found = true
        )
    }
}
