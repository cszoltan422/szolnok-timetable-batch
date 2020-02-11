package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.times
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobInstance
import org.springframework.batch.core.JobParameters
import org.springframework.batch.item.ExecutionContext
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository
import java.time.LocalDateTime
import java.util.Optional

class SaveBatchJobExecutionListenerTest {
    private lateinit var testSubject: SaveBatchJobExecutionListener

    @Mock
    private lateinit var batchJobRepository: BatchJobRepository

    private lateinit var properties: TimetableProperties

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        properties = TimetableProperties()
        properties.selector = TimetableSelectorProperties()

        testSubject = SaveBatchJobExecutionListener(batchJobRepository)
    }

    @Test
    fun `beforeJob should save a new batchJob and add zero as id to the executionContext if the save returned null`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val jobParameters = mock(JobParameters::class.java)
        val executionContext = mock(ExecutionContext::class.java)
        val jobInstance = mock(JobInstance::class.java)

        val argumentCaptor = ArgumentCaptor.forClass(BatchJobEntity::class.java)

        given(jobExecution.status).willReturn(BatchStatus.STARTED)
        given(jobExecution.jobInstance).willReturn(jobInstance)
        given(jobExecution.jobParameters).willReturn(jobParameters)
        given(jobParameters.getString(anyString(), anyString())).willReturn("1,2")
        given(jobInstance.jobName).willReturn("jobName")
        given(jobExecution.executionContext).willReturn(executionContext)
        given(batchJobRepository.saveAndFlush(any(BatchJobEntity::class.java))).willReturn(BatchJobEntity())

        // WHEN
        testSubject.beforeJob(jobExecution)

        // THEN
        verify(jobExecution).jobParameters
        verify(jobParameters).getString("selectedBuses", "")
        verify(jobExecution, times(2)).jobInstance
        verify(jobInstance, times(2)).jobName
        verify(jobExecution).executionContext
        verify(executionContext).putLong("batchJobEntityId", 0L)

        verify(batchJobRepository).saveAndFlush(argumentCaptor.capture())

        assertThat(argumentCaptor.value.startTime).isBeforeOrEqualTo(LocalDateTime.now())
        assertThat(argumentCaptor.value.status).isEqualTo(BatchStatus.STARTED)
        assertThat(argumentCaptor.value.type).isEqualTo("jobName")
        assertThat(argumentCaptor.value.parameters).isEqualTo(listOf("1", "2"))
        assertThat(argumentCaptor.value.finished).isFalse()
    }

    @Test
    fun `beforeJob should save a new batchJob and add the id to the executionContext`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val jobParameters = mock(JobParameters::class.java)
        val executionContext = mock(ExecutionContext::class.java)
        val jobInstance = mock(JobInstance::class.java)

        val argumentCaptor = ArgumentCaptor.forClass(BatchJobEntity::class.java)

        given(jobExecution.status).willReturn(BatchStatus.STARTED)
        given(jobExecution.jobParameters).willReturn(jobParameters)
        given(jobParameters.getString(anyString(), anyString())).willReturn("1,2")
        given(jobExecution.jobInstance).willReturn(jobInstance)
        given(jobInstance.jobName).willReturn("jobName")
        given(jobExecution.executionContext).willReturn(executionContext)
        given(batchJobRepository.saveAndFlush(any(BatchJobEntity::class.java))).willReturn(BatchJobEntity(id = 10L))

        // WHEN
        testSubject.beforeJob(jobExecution)

        // THEN
        verify(jobExecution).jobParameters
        verify(jobParameters).getString("selectedBuses", "")
        verify(jobExecution, times(2)).jobInstance
        verify(jobInstance, times(2)).jobName
        verify(jobExecution).executionContext
        verify(executionContext).putLong("batchJobEntityId", 10L)

        verify(batchJobRepository).saveAndFlush(argumentCaptor.capture())

        assertThat(argumentCaptor.value.startTime).isBeforeOrEqualTo(LocalDateTime.now())
        assertThat(argumentCaptor.value.status).isEqualTo(BatchStatus.STARTED)
        assertThat(argumentCaptor.value.type).isEqualTo("jobName")
        assertThat(argumentCaptor.value.parameters).isEqualTo(listOf("1", "2"))
        assertThat(argumentCaptor.value.finished).isFalse()
    }

    @Test
    fun `afterJob should fetch the batchJobEntityId and not save the entity if not present`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val executionContext = mock(ExecutionContext::class.java)

        given(jobExecution.executionContext).willReturn(executionContext)
        given(executionContext.getLong(anyString(), anyLong())).willReturn(0L)
        given(batchJobRepository.findById(anyLong())).willReturn(Optional.empty())

        // WHEN
        testSubject.afterJob(jobExecution)

        // THEN
        verify(jobExecution).executionContext
        verify(executionContext).getLong("batchJobEntityId", 0L)
        verify(batchJobRepository).findById(0L)
        verify(batchJobRepository, never()).save(any(BatchJobEntity::class.java))
    }

    @Test
    fun `afterJob should fetch the batchJobEntityId and update the existing entity`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val executionContext = mock(ExecutionContext::class.java)
        val entity = BatchJobEntity()

        given(jobExecution.status).willReturn(BatchStatus.COMPLETED)
        given(jobExecution.executionContext).willReturn(executionContext)
        given(executionContext.getLong(anyString(), anyLong())).willReturn(1L)
        given(batchJobRepository.findById(anyLong())).willReturn(Optional.of(entity))

        // WHEN
        testSubject.afterJob(jobExecution)

        // THEN
        verify(jobExecution).executionContext
        verify(executionContext).getLong("batchJobEntityId", 0L)
        verify(batchJobRepository).findById(1L)
        verify(batchJobRepository).save(entity)
        assertThat(entity.finished).isTrue()
        assertThat(entity.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(entity.finishTime).isBeforeOrEqualTo(LocalDateTime.now())
    }
}
