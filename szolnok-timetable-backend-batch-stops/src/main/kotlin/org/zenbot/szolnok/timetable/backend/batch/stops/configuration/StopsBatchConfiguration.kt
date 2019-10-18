package org.zenbot.szolnok.timetable.backend.batch.stops.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.listener.CompositeJobExecutionListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.stops.batch.step.stops.listener.RemoveBusStopsExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.repository.BusStopRepository

@Configuration
@EnableBatchProcessing
class StopsBatchConfiguration(
        private val jobBuilderFactory: JobBuilderFactory,
        private val removeBusStopsExecutionListener: RemoveBusStopsExecutionListener,
        private val batchJobExecutionListener: BatchJobExecutionListener,
        private val readUrlsStep: Step,
        private val saveBusStopsStep: Step
) {

    @Bean
    fun busStopWithBusesJob(busStopRepository: BusStopRepository): Job {
        return jobBuilderFactory
                .get("busStopWithBusesJob")
                .listener(compositeListener())
                .start(readUrlsStep)
                .next(saveBusStopsStep)
                .build()
    }

    @Bean
    fun compositeListener(): JobExecutionListener {
        val compositeJobExecutionListener = CompositeJobExecutionListener()
        val listeners = ArrayList<JobExecutionListener>()
        listeners.add(0, batchJobExecutionListener)
        listeners.add(1, removeBusStopsExecutionListener)
        compositeJobExecutionListener.setListeners(listeners)
        return compositeJobExecutionListener
    }
}