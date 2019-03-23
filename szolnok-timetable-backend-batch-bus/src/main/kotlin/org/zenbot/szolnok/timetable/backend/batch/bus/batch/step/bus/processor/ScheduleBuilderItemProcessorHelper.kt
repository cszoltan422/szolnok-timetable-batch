package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.document.bus.BusArrival
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Schedule

@Component
class ScheduleBuilderItemProcessorHelper {

    private val log = LoggerFactory.getLogger(ScheduleBuilderItemProcessorHelper::class.java)

    fun buildSchedule(timetable: Timetable, weekdayKey: String): Schedule {
        log.debug("Building schedule for day: [{}]", weekdayKey)

        val busArrivalsByHour = timetable.timetable
                .flatMap { (key, value) -> parseTimetableItem(key, value, weekdayKey) }
                .toMutableList()

        return Schedule(busArrivals = busArrivalsByHour)
    }

    private fun parseTimetableItem(hour: Int, value: Map<String, String>, weekdayKey: String): List<BusArrival> {
        val arrivals = value[weekdayKey]
        val arrivalsSplitted = arrivals!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }

        return arrivalsSplitted
                .filter { it != "" }
                .map { createBusArrival(hour, it) }
    }

    private fun createBusArrival(hour: Int, minute: String?): BusArrival = BusArrival(arrivalHour = hour, arrivalMinute = minute?.toInt())

}
