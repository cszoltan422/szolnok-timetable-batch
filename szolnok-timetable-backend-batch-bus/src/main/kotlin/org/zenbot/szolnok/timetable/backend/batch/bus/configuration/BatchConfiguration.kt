package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableProperties::class)
class BatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobExecutionListener: JobExecutionListener,
    private val readUrlsStep: Step,
    private val saveBusStep: Step
) {

    @Bean
    fun importTimetableJob(): Job {
        return jobBuilderFactory
                .get("importTimetableJob")
                .listener(jobExecutionListener)
                .start(readUrlsStep)
                .next(saveBusStep)
                .build()
    }
}
