package org.zenbot.szolnok.timetable.backend.service.jobs

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import org.zenbot.szolnok.timetable.backend.repository.BatchJobRepository

class BatchJobInvalidatorServiceTest {

    @InjectMocks
    private lateinit var testSubject: BatchJobInvalidatorService

    @Mock
    private lateinit var batchJobRepository: BatchJobRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `invalidatePreviousJobs should invalidate jobs with the same parameter list`() {
        // GIVEN
        val batchJob1 = BatchJobEntity(parameters = listOf("PARAM_1"), promotable = true)
        val batchJob2 = BatchJobEntity(parameters = listOf("PARAM_1", "PARAM_2"), promotable = true)

        given(batchJobRepository.findAllByTypeAndFinishedTrueAndPromotableTrue("JOB_TYPE"))
                .willReturn(listOf(batchJob1, batchJob2))

        // WHEN
        testSubject.invalidatePreviousJobs(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1")))

        // THEN
        verify(batchJobRepository).findAllByTypeAndFinishedTrueAndPromotableTrue("JOB_TYPE")
        verify(batchJobRepository).save(batchJob1)

        assertThat(batchJob1.promotable).isFalse()
    }
}
