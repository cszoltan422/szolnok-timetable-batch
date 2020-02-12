package org.zenbot.szolnok.timetable.backend.service.jobs

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.batch.core.BatchStatus
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

class BatchJobValidatorServiceTest {

    @InjectMocks
    private lateinit var testSubject: BatchJobValidatorService

    @Mock
    private lateinit var batchJobRepository: BatchJobRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `validateJobNotRunning should not throw exception if there is no job already running for the same type`() {
        // GIVEN
        given(batchJobRepository.findAllByTypeAndStatusAndFinishedFalse("JOB_TYPE", BatchStatus.STARTED))
                .willReturn(emptyList())

        // WHEN
        testSubject.validateJobNotRunning(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1")))

        // THEN
        Mockito.verify(batchJobRepository).findAllByTypeAndStatusAndFinishedFalse("JOB_TYPE", BatchStatus.STARTED)
    }

    @Test(expected = BatchJobAlreadyRunningException::class)
    fun `validateJobNotRunning should throw exception if there is a job already running for the same type`() {
        // GIVEN
        given(batchJobRepository.findAllByTypeAndStatusAndFinishedFalse("JOB_TYPE", BatchStatus.STARTED))
                .willReturn(listOf(BatchJobEntity()))

        // WHEN
        testSubject.validateJobNotRunning(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1")))

        // THEN exception is thrown
    }
}
