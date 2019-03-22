package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.bus.domain.Bus
import org.zenbot.szolnok.timetable.backend.batch.bus.domain.BusStop
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.domain.Timetable

@Component
class TimetableToBusItemProcessor(private val createBusItemProcessorHelper: CreateBusFromTimetableItemProcessorHelper, private val scheduleBuilderItemProcessorHelper: ScheduleBuilderItemProcessorHelper) : ItemProcessor<Timetable, Bus> {

    private val log = LoggerFactory.getLogger(TimetableToBusItemProcessor::class.java)

    @Throws(Exception::class)
    override fun process(timetable: Timetable): Bus {
        log.info("Processing timetable [#{} from={}, stop={}, to={}] to Bus", timetable.busName, timetable.startBusStopName, timetable.activeStopName, timetable.endBusStopName)

        val busStop = BusStop()
        log.debug("Setting bus stop properties for stop=[{}] and bus=[{}]", timetable.activeStopName, timetable.busName)
        busStop.busStopName = timetable.activeStopName
        busStop.workDaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)
        busStop.saturdaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SATURDAY_KEY)
        busStop.sundaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SUNDAY_KEY)

        val bus = createBusItemProcessorHelper.createBusFromTimetable(timetable)
        log.debug("Fetching bus route from bus=[#{}]", bus.busName)
        val busRoute = bus.getBusRouteByStartStopName(timetable.startBusStopName)
        busRoute.startBusStop = timetable.startBusStopName
        busRoute.endBusStop = timetable.endBusStopName
        busRoute.addBusStopTimetable(busStop)

        if (bus.hasNoBusRoute(busRoute)) {
            bus.busRoutes.add(busRoute)
        }

        log.info("Done processing timetable to bus=[#{}]", bus.busName)
        return bus
    }
}
