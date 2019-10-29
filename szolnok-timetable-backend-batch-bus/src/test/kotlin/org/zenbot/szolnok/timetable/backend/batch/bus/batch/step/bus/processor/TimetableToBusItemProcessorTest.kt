package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.processor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

class TimetableToBusItemProcessorTest {

    @InjectMocks
    private lateinit var testSubject: TimetableToBusItemProcessor

    @Mock
    private lateinit var createBusItemProcessorHelper: CreateBusFromTimetableItemProcessorHelper

    @Mock
    private lateinit var scheduleBuilderItemProcessorHelper: ScheduleBuilderItemProcessorHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `process should return the bus with newly created busRoute and should add to the bus`() {
        // GIVEN
        val timetable = Timetable(
                busName = "busName",
                startBusStopName = "startBusStopName",
                activeStopName = "activeStopName",
                endBusStopName = "endBusStopName"
        )

        val bus = BusEntity(
                busName = "busName",
                busRouteEntities = mutableListOf()
        )

        val weekdaySchedule = ScheduleEntity()
        val saturdaySchedule = ScheduleEntity()
        val sundaySchedule = ScheduleEntity()

        given(createBusItemProcessorHelper.createBusFromTimetable(timetable))
                .willReturn(bus)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)).willReturn(weekdaySchedule)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SATURDAY_KEY)).willReturn(saturdaySchedule)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SUNDAY_KEY)).willReturn(sundaySchedule)

        // WHEN

        val result = testSubject.process(timetable)

        // THEN
        verify(createBusItemProcessorHelper).createBusFromTimetable(timetable)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(
                timetable, JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(
                timetable, JsoupDocumentToTimetableProcessor.SATURDAY_KEY)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(
                timetable, JsoupDocumentToTimetableProcessor.SUNDAY_KEY)

        assertThat(result)
                .isNotNull()
                .isSameAs(bus)

        assertThat(result.busRouteEntities)
                .hasSize(1)
                .contains(
                        BusRouteEntity(
                                startBusStop = "startBusStopName",
                                endBusStop = "endBusStopName",
                                busStopEntities = mutableListOf(
                                        BusStopEntity(
                                                busStopName = "activeStopName",
                                                workDayScheduleEntity = weekdaySchedule,
                                                saturdayScheduleEntity = saturdaySchedule,
                                                sundayScheduleEntity = sundaySchedule
                                        )
                                )
                        )
                )
    }

    @Test
    fun `process should return the bus with the busStop added to the bus route`() {
        // GIVEN
        val timetable = Timetable(
                busName = "busName",
                startBusStopName = "startBusStopName",
                activeStopName = "activeStopName",
                endBusStopName = "endBusStopName"
        )

        val busRouteEntity = BusRouteEntity(
                startBusStop = "startBusStopName",
                endBusStop = "endBusStopName",
                busStopEntities = mutableListOf()
        )
        val bus = BusEntity(
                busName = "busName",
                busRouteEntities = mutableListOf(busRouteEntity)
        )

        val weekdaySchedule = ScheduleEntity()
        val saturdaySchedule = ScheduleEntity()
        val sundaySchedule = ScheduleEntity()

        given(createBusItemProcessorHelper.createBusFromTimetable(timetable))
                .willReturn(bus)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)).willReturn(weekdaySchedule)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SATURDAY_KEY)).willReturn(saturdaySchedule)
        given(scheduleBuilderItemProcessorHelper.buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SUNDAY_KEY)).willReturn(sundaySchedule)

        // WHEN

        val result = testSubject.process(timetable)

        // THEN
        verify(createBusItemProcessorHelper).createBusFromTimetable(timetable)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.WEEKDAY_KEY)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SATURDAY_KEY)
        verify(scheduleBuilderItemProcessorHelper).buildSchedule(timetable,
                JsoupDocumentToTimetableProcessor.SUNDAY_KEY)

        assertThat(result)
                .isNotNull()
                .isSameAs(bus)

        assertThat(result.busRouteEntities)
                .hasSize(1)
                .contains(busRouteEntity)

        assertThat(result.busRouteEntities[0].busStopEntities)
                .hasSize(1)
                .contains(BusStopEntity(
                        busStopName = "activeStopName",
                        workDayScheduleEntity = weekdaySchedule,
                        saturdayScheduleEntity = saturdaySchedule,
                        sundayScheduleEntity = sundaySchedule
                ))
    }
}
