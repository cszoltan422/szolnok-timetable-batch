package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@ComponentScan(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.batch.bus",
        "org.zenbot.szolnok.timetable.backend.batch.stops"))
@EnableBatchProcessing
class BatchJobsConfiguration {

    @Bean
    fun defaultBatchConfigurer(
            @Qualifier("embeddedDataSource") embeddedDataSource: DataSource,
            transactionManager: PlatformTransactionManager
    ) = PlatformTransactionManagerBasedBatchConfigurer(embeddedDataSource, transactionManager)
}
