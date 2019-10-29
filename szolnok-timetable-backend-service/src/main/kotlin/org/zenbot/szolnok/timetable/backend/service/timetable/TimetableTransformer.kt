package org.zenbot.szolnok.timetable.backend.service.timetable

import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.ScheduleResponse
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.TimetableResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity

/**
 * Transforms a [BusEntity], [BusRouteEntity], [BusStopEntity] into a [TimetableResponse]
 */
@Component
class TimetableTransformer(
    private val scheduleTransformer: ScheduleTransformer
) {

    /**
     * Creates an empty [TimetableResponse]
     * @return an empty [TimetableResponse]
     */
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

    /**
     * Transforms a [BusEntity], [BusRouteEntity], [BusStopEntity] into a [TimetableResponse]
     * @param bus The bus to use in the transformation
     * @param busRoute The route to use in the transformation
     * @param busStop The busStop to use in the transformation
     * @param occurrence The occurrence of the busStop in the current route
     * @return a [TimetableResponse] from the parameters
     */
    fun transform(
        bus: BusEntity,
        busRoute: BusRouteEntity,
        busStop: BusStopEntity,
        occurrence: Int
    ): TimetableResponse {
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
