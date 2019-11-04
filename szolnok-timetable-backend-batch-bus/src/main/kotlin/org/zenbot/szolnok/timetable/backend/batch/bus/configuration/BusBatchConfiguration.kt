package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.listener.CompositeJobExecutionListener
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.bus.batch.step.bus.listener.RemoveBusRoutesExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties

@Configuration
@EnableConfigurationProperties(TimetableProperties::class)
class BusBatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val removeBusRoutesExecutionListener: RemoveBusRoutesExecutionListener,
    private val batchJobExecutionListener: BatchJobExecutionListener,
    private val readUrlsStep: Step,
    private val saveBusStep: Step
) {

    @Bean
    fun szolnokBusesJob(): Job {
        val compositeJobExecutionListener = CompositeJobExecutionListener()
        val listeners = ArrayList<JobExecutionListener>()
        listeners.add(0, batchJobExecutionListener)
        listeners.add(1, removeBusRoutesExecutionListener)
        compositeJobExecutionListener.setListeners(listeners)

        return jobBuilderFactory
                .get("szolnokBusesJob")
                .listener(compositeJobExecutionListener)
                .start(readUrlsStep)
                .next(saveBusStep)
                .build()
    }
}
