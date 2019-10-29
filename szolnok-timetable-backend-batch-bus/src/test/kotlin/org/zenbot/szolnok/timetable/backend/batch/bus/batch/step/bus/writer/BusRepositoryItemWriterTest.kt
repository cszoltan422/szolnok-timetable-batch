package org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.writer

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.item.ExecutionContext
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import org.zenbot.szolnok.timetable.backend.repository.BusRepository
import java.util.Optional

class BusRepositoryItemWriterTest {

    @InjectMocks
    private lateinit var testSubject: BusRepositoryItemWriter

    @Mock
    private lateinit var busRepository: BusRepository

    @Mock
    private lateinit var batchJobRepository: BatchJobRepository

    @Mock
    private lateinit var stepExecution: StepExecution

    @Mock
    private lateinit var jobExecution: JobExecution

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        given(stepExecution.jobExecution).willReturn(jobExecution)

        testSubject.before(stepExecution)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `write should throw exception if the list size is more than 1`() {
        // GIVEN
        val bus = BusEntity(busName = "busName")
        val bus2 = BusEntity(busName = "busName")

        // WHEN
        testSubject.write(listOf(bus, bus2))

        // THEN exception in thrown
    }

    @Test
    fun `write should fetch the current batch job but not set if not present`() {
        // GIVEN
        val bus = BusEntity(busName = "busName")
        val executionContext = Mockito.mock(ExecutionContext::class.java)

        given(jobExecution.executionContext).willReturn(executionContext)
        given(executionContext.getLong(anyString(), anyLong())).willReturn(0L)
        given(batchJobRepository.findById(anyLong())).willReturn(Optional.empty())

        // WHEN
        testSubject.write(listOf(bus))

        // THEN
        verify(stepExecution).jobExecution
        verify(jobExecution).executionContext
        verify(executionContext).getLong(
                BatchJobExecutionListener.BATCH_JOB_ENTITY_ID_KEY,
                BatchJobExecutionListener.DEFAULT_BATCH_JOB_ENTITY_ID_VALUE)
        verify(batchJobRepository).findById(0L)
        verify(busRepository).save(bus)
    }

    @Test
    fun `write should fetch the current batch job and add the result to the bus`() {
        // GIVEN
        val bus = BusEntity(busName = "busName")
        val executionContext = Mockito.mock(ExecutionContext::class.java)
        val batchJob = BatchJobEntity()

        given(jobExecution.executionContext).willReturn(executionContext)
        given(executionContext.getLong(anyString(), anyLong())).willReturn(0L)
        given(batchJobRepository.findById(anyLong())).willReturn(Optional.of(batchJob))

        // WHEN
        testSubject.write(listOf(bus))

        // THEN
        verify(stepExecution).jobExecution
        verify(jobExecution).executionContext
        verify(executionContext).getLong(
                BatchJobExecutionListener.BATCH_JOB_ENTITY_ID_KEY,
                BatchJobExecutionListener.DEFAULT_BATCH_JOB_ENTITY_ID_VALUE)
        verify(batchJobRepository).findById(0L)
        verify(busRepository).save(bus)

        assertThat(bus.batchJobEntity)
                .isNotNull()
                .isSameAs(batchJob)
    }
}
