package org.zenbot.szolnok.timetable.backend.service.cleanup

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

class BatchBusCleanupServiceTest {

    @InjectMocks
    private lateinit var testSubject: BatchBusCleanupService

    @Mock
    private lateinit var busRepository: BusRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `removePreviousBuses should remove all buses if job parameters is empty`() {
        // GIVEN

        // WHEN
        testSubject.removePreviousBuses(LaunchJobRequest(jobType = "JOB_TYPE", parameters = emptyList()))

        // THEN
        Mockito.verify(busRepository).deleteAllByTargetState(TargetState.BATCH)
        Mockito.verify(busRepository, Mockito.never()).delete(Mockito.any(BusEntity::class.java))
        Mockito.verify(busRepository, Mockito.never()).findAllByTargetState(TargetState.BATCH)
    }

    @Test
    fun `removePreviousBuses should not remove the bus which is not present in the selected buses list`() {
        // GIVEN
        val buses = listOf(BusEntity(busName = "2", targetState = TargetState.BATCH))
        BDDMockito.given(busRepository.findAllByTargetState(TargetState.BATCH)).willReturn(buses)

        // WHEN
        testSubject.removePreviousBuses(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("1")))

        // THEN
        Mockito.verify(busRepository).findAllByTargetState(TargetState.BATCH)
        Mockito.verify(busRepository, Mockito.never()).deleteAllByTargetState(TargetState.BATCH)
        Mockito.verify(busRepository, Mockito.never()).delete(Mockito.any(BusEntity::class.java))
    }

    @Test
    fun `removePreviousBuses should remove the bus which is present in the selected buses list`() {
        // GIVEN
        val bus = BusEntity(busName = "1", targetState = TargetState.BATCH)
        val buses = mutableListOf(bus)

        BDDMockito.given(busRepository.findAllByTargetState(TargetState.BATCH)).willReturn(buses)

        // WHEN
        testSubject.removePreviousBuses(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("1")))

        // THEN
        Mockito.verify(busRepository).findAllByTargetState(TargetState.BATCH)
        Mockito.verify(busRepository).delete(bus)
        Mockito.verify(busRepository, Mockito.never()).deleteAllByTargetState(TargetState.BATCH)
    }
}
