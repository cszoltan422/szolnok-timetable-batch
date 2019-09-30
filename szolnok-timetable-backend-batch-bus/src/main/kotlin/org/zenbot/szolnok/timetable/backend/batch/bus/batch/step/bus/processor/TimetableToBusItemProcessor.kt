package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity

@Component
class TimetableToBusItemProcessor(private val createBusItemProcessorHelper: CreateBusFromTimetableItemProcessorHelper, private val scheduleBuilderItemProcessorHelper: ScheduleBuilderItemProcessorHelper) : ItemProcessor<Timetable, BusEntity> {

    private val log = LoggerFactory.getLogger(TimetableToBusItemProcessor::class.java)

    @Throws(Exception::class)
    override fun process(timetable: Timetable): BusEntity {
        log.info("Processing timetable [#{} from={}, stop={}, to={}] to Bus", timetable.busName, timetable.startBusStopName, timetable.activeStopName, timetable.endBusStopName)

        log.debug("Setting bus stop properties for stop=[{}] and bus=[{}]", timetable.activeStopName, timetable.busName)
        val busStop = createBusStop(timetable)
        val bus = createBusItemProcessorHelper.createBusFromTimetable(timetable)

        log.debug("Fetching bus route from bus=[#{}]", bus.busName)
        val busRoute = createBusRoute(bus, timetable, busStop)

        if (bus.hasNoBusRoute(busRoute)) {
            bus.busRouteEntities.add(busRoute)
        }

        log.info("Done processing timetable to bus=[#{}]", bus.busName)
        return bus
    }

    private fun createBusStop(timetable: Timetable): BusStopEntity {
        val busStopName = timetable.activeStopName
        val workDaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)
        val saturdaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SATURDAY_KEY)
        val sundaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SUNDAY_KEY)

        return BusStopEntity(
                busStopName = busStopName,
                workDayScheduleEntity = workDaySchedule,
                saturdayScheduleEntity = saturdaySchedule,
                sundayScheduleEntity = sundaySchedule
        )
    }

    private fun createBusRoute(busEntity: BusEntity, timetable: Timetable, busStopEntity: BusStopEntity): BusRouteEntity {
        val busRoute = busEntity.getBusRouteByStartStopName(timetable.startBusStopName)
        busRoute.startBusStop = timetable.startBusStopName
        busRoute.endBusStop = timetable.endBusStopName
        busRoute.addBusStopTimetable(busStopEntity)
        return busRoute
    }
}
