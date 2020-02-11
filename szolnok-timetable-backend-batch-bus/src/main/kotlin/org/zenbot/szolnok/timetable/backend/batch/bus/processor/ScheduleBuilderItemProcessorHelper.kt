package org.zenbot.szolnok.timetable.backend.batch.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusArrivalEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

/**
 * Builds the schedule for the busStop from the given [Timetable] parameter
 */
@Component
class ScheduleBuilderItemProcessorHelper {

    private val log = LoggerFactory.getLogger(ScheduleBuilderItemProcessorHelper::class.java)

    /**
     * Builds the schedule for the busStop from the given [Timetable] parameter
     * @param timetable The current timetable to process
     * @param weekdayKey The weekday to build the schedule for
     * @return A [ScheduleEntity] object built from the [Timetable] parameter and for the given weekdayKey
     */
    fun buildSchedule(timetable: Timetable, weekdayKey: String): ScheduleEntity {
        log.debug("Building schedule for day: [{}]", weekdayKey)
        val busArrivalsByHour = ArrayList<BusArrivalEntity>()
        for ((hour, arrivals) in timetable.timetable) {
            val builtArrivals = buildArrivalsForKey(hour, arrivals[weekdayKey])
            busArrivalsByHour.addAll(builtArrivals)
        }
        return ScheduleEntity(busArrivalEntities = busArrivalsByHour)
    }

    private fun buildArrivalsForKey(hour: Int, arrivals: String?): List<BusArrivalEntity> {
        val result = ArrayList<BusArrivalEntity>()
        if (arrivals != null) {
            val arrivalsSplitted = splitArrivals(arrivals)
            val arrivalEntities = createArrivalForEachValue(arrivalsSplitted, hour)
            result.addAll(arrivalEntities)
        }
        return result
    }

    private fun createArrivalForEachValue(arrivalsSplitted: Array<String>, hour: Int): List<BusArrivalEntity> {
        val result = ArrayList<BusArrivalEntity>()
        for (arrival in arrivalsSplitted) {
            if (!arrival.isEmpty()) {
                result.add(createArrival(hour, arrival))
            }
        }
        return result
    }

    private fun createArrival(hour: Int, arrival: String) =
            BusArrivalEntity(arrivalHour = hour,
                    arrivalMinute = Integer.valueOf(arrival)
            )

    private fun splitArrivals(arrivals: String) =
            arrivals.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}
