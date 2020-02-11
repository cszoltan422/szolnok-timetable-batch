package org.zenbot.szolnok.timetable.backend.batch.bus.processor

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity

/**
 * Creates a [BusEntity] from a given [Timetable] to save into the database
 */
@Component
@Transactional
class TimetableToBusItemProcessor(
    private val createBusItemProcessorHelper: CreateBusFromTimetableItemProcessorHelper,
    private val scheduleBuilderItemProcessorHelper: ScheduleBuilderItemProcessorHelper
) : ItemProcessor<Timetable, BusEntity> {

    private val log = LoggerFactory.getLogger(TimetableToBusItemProcessor::class.java)

    /**
     * Creates a [BusEntity] from a given [Timetable] to save into the database
     * @param timetable The current timetable to process
     * @return a [BusEntity] created from the [Timetable] parameter
     */
    @Throws(Exception::class)
    override fun process(timetable: Timetable): BusEntity {
        log.info("Processing timetable [#{} from={}, stop={}, to={}] to Bus",
                timetable.busName,
                timetable.startBusStopName,
                timetable.activeStopName,
                timetable.endBusStopName)

        val bus = createBusItemProcessorHelper.createBusFromTimetable(timetable)
        val busStop = createBusStop(timetable)
        val busRoute = createOrGetBusRoute(bus, timetable)
        addBusStopToRoute(busRoute, busStop)
        addRouteIfNotPresent(bus, busRoute)

        log.info("Done processing timetable to bus=[#{}]", bus.busName)
        return bus
    }

    private fun addRouteIfNotPresent(bus: BusEntity, busRoute: BusRouteEntity) {
        if (bus.hasNoBusRoute(busRoute)) {
            bus.busRouteEntities.add(busRoute)
        }
    }

    private fun addBusStopToRoute(busRoute: BusRouteEntity, busStop: BusStopEntity) {
        busRoute.busStopEntities.add(busStop)
    }

    private fun createBusStop(timetable: Timetable): BusStopEntity {
        val busStopName = timetable.activeStopName
        val workDaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)

        val saturdaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SATURDAY_KEY)

        val sundaySchedule = scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SUNDAY_KEY)

        return BusStopEntity(
                busStopName = busStopName,
                workDayScheduleEntity = workDaySchedule,
                saturdayScheduleEntity = saturdaySchedule,
                sundayScheduleEntity = sundaySchedule
        )
    }

    private fun createOrGetBusRoute(busEntity: BusEntity, timetable: Timetable):
            BusRouteEntity {
        val busRoute = busEntity.getBusRouteByStartStopName(timetable.startBusStopName)
        busRoute.startBusStop = timetable.startBusStopName
        busRoute.endBusStop = timetable.endBusStopName
        return busRoute
    }
}
