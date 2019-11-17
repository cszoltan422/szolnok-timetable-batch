package org.zenbot.szolnok.timetable.backend.service.timetable

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.api.timetable.ScheduleResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

class TimetableTransformerTest {

    @InjectMocks
    private lateinit var testSubject: TimetableTransformer

    @Mock
    private lateinit var scheduleTransformer: ScheduleTransformer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `empty should return an empty result`() {
        // GIVEN

        // WHEN
        val result = testSubject.empty()

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "")
                .hasFieldOrPropertyWithValue("startStop", "")
                .hasFieldOrPropertyWithValue("endStop", "")
                .hasFieldOrPropertyWithValue("busStopName", "")
                .hasFieldOrPropertyWithValue("occurrence", 0)
                .hasFieldOrPropertyWithValue("workDaySchedule", ScheduleResponse(emptyList()))
                .hasFieldOrPropertyWithValue("saturdaySchedule", ScheduleResponse(emptyList()))
                .hasFieldOrPropertyWithValue("sundaySchedule", ScheduleResponse(emptyList()))
                .hasFieldOrPropertyWithValue("found", false)
    }

    @Test
    fun `transform should transform the entity into a response object`() {
        // GIVEN
        val bus = BusEntity(busName = "busName")
        val busRoute = BusRouteEntity(
                startBusStop = "startBusStop",
                endBusStop = "endBusStop"
        )
        val workDayScheduleEntity = ScheduleEntity(id = 1L)
        val saturdayScheduleEntity = ScheduleEntity(id = 2L)
        val sundayScheduleEntity = ScheduleEntity(id = 3L)
        val busStop = BusStopEntity(
                busStopName = "busStopName",
                workDayScheduleEntity = workDayScheduleEntity,
                saturdayScheduleEntity = saturdayScheduleEntity,
                sundayScheduleEntity = sundayScheduleEntity
        )
        val workDayScheduleResponse = ScheduleResponse(busArrivals = emptyList())
        val saturdaySchedulResponse = ScheduleResponse(busArrivals = emptyList())
        val sundayScheduleResponse = ScheduleResponse(busArrivals = emptyList())
        given(scheduleTransformer.transform(workDayScheduleEntity)).willReturn(workDayScheduleResponse)
        given(scheduleTransformer.transform(saturdayScheduleEntity)).willReturn(saturdaySchedulResponse)
        given(scheduleTransformer.transform(sundayScheduleEntity)).willReturn(sundayScheduleResponse)

        // WHEN
        val result = testSubject.transform(bus, busRoute, busStop, 2)

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("startStop", "startBusStop")
                .hasFieldOrPropertyWithValue("endStop", "endBusStop")
                .hasFieldOrPropertyWithValue("busStopName", "busStopName")
                .hasFieldOrPropertyWithValue("occurrence", 2)
                .hasFieldOrPropertyWithValue("workDaySchedule", workDayScheduleResponse)
                .hasFieldOrPropertyWithValue("saturdaySchedule", saturdaySchedulResponse)
                .hasFieldOrPropertyWithValue("sundaySchedule", sundayScheduleResponse)
                .hasFieldOrPropertyWithValue("found", true)

        verify(scheduleTransformer).transform(workDayScheduleEntity)
        verify(scheduleTransformer).transform(saturdayScheduleEntity)
        verify(scheduleTransformer).transform(sundayScheduleEntity)
    }
}