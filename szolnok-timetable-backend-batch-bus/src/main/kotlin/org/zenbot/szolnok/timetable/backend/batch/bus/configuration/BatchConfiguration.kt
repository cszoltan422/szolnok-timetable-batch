package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener.RemoveBusRoutesExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.bus.dao.BusRepository
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableProperties::class)
class BatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val timetableProperties: TimetableProperties,
    private val readUrlsStep: Step,
    private val saveBusStep: Step
) {

    @Bean
    fun importTimetableJob(busRepository: BusRepository): Job {
        return jobBuilderFactory
                .get("importTimetableJob")
                .listener(jobExecutionListener(busRepository))
                .start(readUrlsStep)
                .next(saveBusStep)
                .build()
    }

    @Bean
    fun jobExecutionListener(busRepository: BusRepository): JobExecutionListener {
        return RemoveBusRoutesExecutionListener(busRepository, timetableProperties.resource)
    }
}
