package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.bus.domain.BusArrival
import org.zenbot.szolnok.timetable.backend.batch.bus.domain.Schedule
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable

@Component
class ScheduleBuilderItemProcessorHelper {

    private val log = LoggerFactory.getLogger(ScheduleBuilderItemProcessorHelper::class.java)

    fun buildSchedule(timetable: Timetable, weekdayKey: String): Schedule {
        log.debug("Building schedule for day: [{}]", weekdayKey)
        val busArrivalsByHour = ArrayList<BusArrival>()
        for ((key, value) in timetable.timetable) {
            val arrivals = value[weekdayKey]
            val arrivalsSplitted = arrivals!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val busArrivals = ArrayList<BusArrival>()
            for (arrival in arrivalsSplitted) {
                if ("" != arrival) {
                    val busArrival = BusArrival()
                    busArrival.arrivalHour = key
                    busArrival.arrivalMinute = if (arrival.isEmpty()) null else Integer.valueOf(arrival)
                    busArrivals.add(busArrival)
                }
            }
            busArrivalsByHour.addAll(busArrivals)
        }
        val schedule = Schedule()
        schedule.busArrivals = busArrivalsByHour
        return schedule
    }
}
