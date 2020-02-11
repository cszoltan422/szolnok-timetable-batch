package org.zenbot.szolnok.timetable.backend.batch.bus.processor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

class CreateBusFromTimetableItemProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject: CreateBusFromTimetableItemProcessorHelper

    @Mock
    private lateinit var busRepository: BusRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `createBusFromTimetable should return a new BusEntity if there's no such bus in the database`() {
        // GIVEN
        val timetable = Timetable(busName = "busName")

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH))
                .willReturn(null)

        // WHEN
        val result = testSubject.createBusFromTimetable(timetable)

        // THEN
        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.BATCH)

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("targetState", TargetState.BATCH)
    }

    @Test
    fun `createBusFromTimetable should return the same instance from the database if present`() {
        // GIVEN
        val timetable = Timetable(busName = "busName")
        val bus = BusEntity()

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH))
                .willReturn(bus)

        // WHEN
        val result = testSubject.createBusFromTimetable(timetable)

        // THEN
        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.BATCH)

        assertThat(result)
                .isNotNull()
                .isSameAs(bus)
    }
}
