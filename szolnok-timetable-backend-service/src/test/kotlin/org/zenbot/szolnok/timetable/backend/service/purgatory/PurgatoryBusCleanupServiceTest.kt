package org.zenbot.szolnok.timetable.backend.service.purgatory

import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

class PurgatoryBusCleanupServiceTest {

    @InjectMocks
    private lateinit var testSubject: PurgatoryBusCleanupService

    @Mock
    private lateinit var busRepository: BusRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `cleanUpPurgatoryBuses should call the busRepository to remove all buses from purgatory target state`() {
        // GIVEN

        // WHEN
        testSubject.cleanUpPurgatoryBuses()

        // THEN
        verify(busRepository).deleteAllByTargetState(TargetState.PURGATORY)
    }
}
