package org.zenbot.szolnok.timetable.backend.service.bus

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

class BusServiceTest {

    @InjectMocks
    private lateinit var testSubject: BusService

    @Mock
    private lateinit var busRepository: BusRepository

    @Mock
    private lateinit var busEntityTransformer: BusEntityTransformer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `findAllByTargetState should query all by target state and transform into response object`() {
        // GIVEN
        val entities = listOf(BusEntity(), BusEntity(), BusEntity(), BusEntity())
        given(
                busRepository.findAllByBusNameContainsAndTargetStateAndBatchJobEntityFinishedTrue("", TargetState.PRODUCTION)
        ).willReturn(entities)
        given(busEntityTransformer.transform(BusEntity())).willReturn(BusResponse(
                busName = "busName",
                startBusStop = "startBusStop",
                endBusStop = "endBusStop",
                batchJobId = 10L))

        // WHEN
        val result = testSubject.findAllByTargetState("", TargetState.PRODUCTION)

        // THEN
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)

        verify(busRepository).findAllByBusNameContainsAndTargetStateAndBatchJobEntityFinishedTrue("", TargetState.PRODUCTION)
        verify(busEntityTransformer, times(4)).transform(BusEntity())
    }

    @Test(expected = BusNotFoundException::class)
    fun `removeBusFromBatchTargetState should throw BusNotFoundException if bus is not found by name and target state`() {
        // GIVEN
        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH)).willReturn(null)

        // WHEN
        testSubject.removeBusFromBatchTargetState("busName")

        // THEN exception is thrown...
    }

    @Test(expected = BatchJobOfBusInProgressException::class)
    fun `removeBusFromBatchTargetState should throw BatchJobOfBusInProgressException if the job is null`() {
        // GIVEN
        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH)).willReturn(BusEntity(
                id = 12L,
                busName = "busName",
                batchJobEntity = null
        ))

        // WHEN
        testSubject.removeBusFromBatchTargetState("busName")

        // THEN exception is thrown...
    }

    @Test(expected = BatchJobOfBusInProgressException::class)
    fun `removeBusFromBatchTargetState should throw BatchJobOfBusInProgressException if the job is in progess`() {
        // GIVEN
        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH)).willReturn(BusEntity(
                id = 12L,
                busName = "busName",
                batchJobEntity = BatchJobEntity(finished = false)
        ))

        // WHEN
        testSubject.removeBusFromBatchTargetState("busName")

        // THEN exception is thrown...
    }

    @Test
    fun `removeBusFromBatchTargetState should delete the bus if found and the job is finished`() {
        // GIVEN
        given(busRepository.findByBusNameAndTargetState("busName", TargetState.BATCH)).willReturn(BusEntity(
                id = 12L,
                busName = "busName",
                batchJobEntity = BatchJobEntity(finished = true)
        ))

        // WHEN
        testSubject.removeBusFromBatchTargetState("busName")

        // THEN
        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.BATCH)
        verify(busRepository).deleteByBusNameAndTargetState("busName", TargetState.BATCH)
    }
}
