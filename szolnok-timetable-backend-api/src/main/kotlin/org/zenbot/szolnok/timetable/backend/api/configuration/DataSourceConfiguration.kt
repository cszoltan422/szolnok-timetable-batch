package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.zenbot.szolnok.timetable.backend.api.configuration.properties.HibernateProperties
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(HibernateProperties::class)
class DataSourceConfiguration(
        private val properties: HibernateProperties
) {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
                .url(properties.url)
                .username(properties.username)
                .password(properties.password)
                .driverClassName(properties.driverClassName)
                .build()
    }

    @Bean
    @Primary
    @Qualifier("embeddedDataSource")
    fun secondaryDataSource(): DataSource {
        return EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
    }
}