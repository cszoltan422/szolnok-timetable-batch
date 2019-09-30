package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusArrivalEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

@Component
class ScheduleBuilderItemProcessorHelper {

    private val log = LoggerFactory.getLogger(ScheduleBuilderItemProcessorHelper::class.java)

    fun buildSchedule(timetable: Timetable, weekdayKey: String): ScheduleEntity {
        log.debug("Building schedule for day: [{}]", weekdayKey)
        val busArrivalsByHour = ArrayList<BusArrivalEntity>()
        for ((key, value) in timetable.timetable) {
            val arrivals = value[weekdayKey]
            val arrivalsSplitted = arrivals!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val busArrivals = ArrayList<BusArrivalEntity>()
            for (arrival in arrivalsSplitted) {
                if ("" != arrival) {
                    val busArrival = BusArrivalEntity()
                    busArrival.arrivalHour = key
                    busArrival.arrivalMinute = if (arrival.isEmpty()) null else Integer.valueOf(arrival)
                    busArrivals.add(busArrival)
                }
            }
            busArrivalsByHour.addAll(busArrivals)
        }
        val schedule = ScheduleEntity()
        schedule.busArrivalEntities = busArrivalsByHour
        return schedule
    }
}
