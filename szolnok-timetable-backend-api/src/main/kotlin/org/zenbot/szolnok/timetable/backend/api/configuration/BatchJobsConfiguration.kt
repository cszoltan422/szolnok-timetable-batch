package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@ComponentScan(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.batch.bus",
        "org.zenbot.szolnok.timetable.backend.batch.stops"))
class BatchJobsConfiguration {

    @Bean
    fun defaultBatchConfigurer(@Qualifier("embeddedDataSource") embeddedDataSource: DataSource) = DefaultBatchConfigurer(embeddedDataSource)
}