package org.zenbot.szolnok.timetable.backend.service.timetable

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusArrivalEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.ScheduleEntity

class ScheduleTransformerTest {

    private lateinit var testSubject: ScheduleTransformer

    @Before
    fun setUp() {
        testSubject = ScheduleTransformer()
    }

    @Test
    fun `transform should transform the entity into a response object`() {
        // GIVEN
        val schedule = ScheduleEntity(
                busArrivalEntities = mutableListOf(
                        BusArrivalEntity(
                                arrivalHour = 10,
                                arrivalMinute = 10,
                                isLowfloor = true
                        ),
                        BusArrivalEntity(
                                arrivalHour = 10,
                                arrivalMinute = 15,
                                isLowfloor = false
                        )
                )
        )

        // WHEN
        val result = testSubject.transform(schedule)

        // THEN
        assertThat(result)
                .isNotNull()

        assertThat(result.busArrivals)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)

        assertThat(result.busArrivals)
                .element(0)
                .isNotNull()
                .hasFieldOrPropertyWithValue("arrivalHour", 10)
                .hasFieldOrPropertyWithValue("arrivalMinute", 10)
                .hasFieldOrPropertyWithValue("isLowfloor", true)

        assertThat(result.busArrivals)
                .element(1)
                .isNotNull()
                .hasFieldOrPropertyWithValue("arrivalHour", 10)
                .hasFieldOrPropertyWithValue("arrivalMinute", 15)
                .hasFieldOrPropertyWithValue("isLowfloor", false)
    }
}