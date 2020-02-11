package org.zenbot.szolnok.timetable.backend.batch.bus.processor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusArrivalEntity

class ScheduleBuilderItemProcessorHelperTest {

    private lateinit var testSubject: ScheduleBuilderItemProcessorHelper

    @Before
    fun setUp() {
        testSubject = ScheduleBuilderItemProcessorHelper()
    }

    @Test
    fun `buildSchedule should return empty ScheduleEntity if there is no data for the weekdayKey`() {
        // GIVEN
        val timetable = Timetable(
                timetable = mutableMapOf(1 to mapOf("sunday" to "1,2,3"))
        )

        // WHEN
        val result = testSubject.buildSchedule(timetable, "weekday")

        // THEN
        assertThat(result).isNotNull()
        assertThat(result.busArrivalEntities)
                .isNotNull()
                .isEmpty()
    }

    @Test
    fun `buildSchedule should return empty ScheduleEntity if arrivals are empty for the weekdayKey`() {
        // GIVEN
        val timetable = Timetable(
                timetable = mutableMapOf(1 to mapOf("weekday" to ""))
        )

        // WHEN
        val result = testSubject.buildSchedule(timetable, "weekday")

        // THEN
        assertThat(result).isNotNull()
        assertThat(result.busArrivalEntities)
                .isNotNull()
                .isEmpty()
    }

    @Test
    fun `buildSchedule should return a parsed ScheduleEntity for single hour`() {
        // GIVEN
        val timetable = Timetable(
                timetable = mutableMapOf(1 to mapOf("weekday" to "1,2,3"))
        )

        // WHEN
        val result = testSubject.buildSchedule(timetable, "weekday")

        // THEN
        assertThat(result).isNotNull()
        assertThat(result.busArrivalEntities)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyInAnyOrder(
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 1),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 2),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 3)
                )
    }

    @Test
    fun `buildSchedule should return a parsed ScheduleEntity for multiple hour`() {
        // GIVEN
        val timetable = Timetable(
                timetable = mutableMapOf(
                        1 to mapOf("weekday" to "1,2,3"),
                        2 to mapOf("weekday" to "4,5,6"),
                        3 to mapOf("sunday" to "3,4"),
                        4 to mapOf("weekday" to "")
                )
        )

        // WHEN
        val result = testSubject.buildSchedule(timetable, "weekday")

        // THEN
        assertThat(result).isNotNull()
        assertThat(result.busArrivalEntities)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyInAnyOrder(
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 1),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 2),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 1, arrivalMinute = 3),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 2, arrivalMinute = 4),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 2, arrivalMinute = 5),
                        BusArrivalEntity(isLowfloor = false, arrivalHour = 2, arrivalMinute = 6)
                )
    }
}
