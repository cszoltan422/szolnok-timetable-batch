package org.zenbot.szolnok.timetable.backend.service.jobs

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.SaveBatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.domain.api.jobs.LaunchJobRequest

class BatchJobLauncherServiceTest {

    @InjectMocks
    private lateinit var testSubject: BatchJobLauncherService

    @Mock
    private lateinit var batchJobValidatorService: BatchJobValidatorService

    @Mock
    private lateinit var batchJobInvalidatorService: BatchJobInvalidatorService

    @Mock
    private lateinit var jobLauncher: JobLauncher

    @Mock
    private lateinit var batchJobMap: Map<String, Job>

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<JobParameters>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(expected = NoSuchBatchJobException::class)
    fun `launch should throw exception if there is no such job for the given type`() {
        // GIVEN
        given(batchJobMap["JOB_TYPE"]).willReturn(null)

        // WHEN
        testSubject.launch(LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1")))

        // THEN exception is thrown...
    }

    @Test(expected = BatchJobAlreadyRunningException::class)
    fun `launch should throw exception if there is a job already running for the same type`() {
        // GIVEN
        val job = mock(Job::class.java)
        val request = LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1"))

        given(batchJobMap["JOB_TYPE"]).willReturn(job)
        given(batchJobValidatorService.validateJobNotRunning(request)).willThrow(BatchJobAlreadyRunningException::class.java)

        // WHEN
        testSubject.launch(request)

        // THEN exception is thrown...
    }

    @Test
    fun `launch should launch a new job if job is present and there is no running for the given type`() {
        // GIVEN
        val job = mock(Job::class.java)
        val request = LaunchJobRequest(jobType = "JOB_TYPE", parameters = listOf("PARAM_1", "PARAM_2"))

        given(batchJobMap["JOB_TYPE"]).willReturn(job)

        // WHEN
        val result = testSubject.launch(request)

        // THEN
        assertThat(result).isNotNull
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("message", "Start")
        verify(batchJobValidatorService).validateJobNotRunning(request)
        verify(batchJobInvalidatorService).invalidatePreviousJobs(request)
        Thread.sleep(500)
        Mockito.verify(jobLauncher).run(ArgumentMatchers.eq(job), argumentCaptor.capture())

        assertThat(argumentCaptor.value)
                .isNotNull
        assertThat(argumentCaptor.value.getString(SaveBatchJobExecutionListener.SELECTED_BUSES_JOB_PARAMETER_KEY))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("PARAM_1,PARAM_2")

        assertThat(argumentCaptor.value.getLong("timestamp"))
                .isNotNull()
    }
}
