package org.zenbot.szolnok.timetable.batch.stops.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.stops.batch.step.stops.listener.RemoveBusStopsExecutionListener
import org.zenbot.szolnok.timetable.batch.stops.dao.BusStopRepository

@Configuration
@EnableBatchProcessing
class BatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val readUrlsStep: Step,
    private val saveBusStopsStep: Step
) {

    @Bean
    fun busStopWithBusesJob(busStopRepository: BusStopRepository): Job {
        return jobBuilderFactory
                .get("busStopWithBusesJob")
                .listener(jobExecutionListener(busStopRepository))
                .start(readUrlsStep)
                .next(saveBusStopsStep)
                .build()
    }

    @Bean
    fun jobExecutionListener(busStopRepository: BusStopRepository): JobExecutionListener {
        return RemoveBusStopsExecutionListener(busStopRepository)
    }
}