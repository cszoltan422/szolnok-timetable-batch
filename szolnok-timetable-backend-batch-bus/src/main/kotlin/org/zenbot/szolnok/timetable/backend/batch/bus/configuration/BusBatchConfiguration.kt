package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.bus.batch.listener.RemoveBusRoutesExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableProperties::class)
class BusBatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobExecutionListener: RemoveBusRoutesExecutionListener,
    private val readUrlsStep: Step,
    private val saveBusStep: Step
) {

    @Bean
    fun szolnokBusesJob(): Job {
        return jobBuilderFactory
                .get("szolnokBusesJob")
                .listener(jobExecutionListener)
                .start(readUrlsStep)
                .next(saveBusStep)
                .build()
    }

    @Bean
    fun defaultBatchConfigurer(@Qualifier("embeddedDataSource") embeddedDataSource: DataSource) = DefaultBatchConfigurer(embeddedDataSource)
}
