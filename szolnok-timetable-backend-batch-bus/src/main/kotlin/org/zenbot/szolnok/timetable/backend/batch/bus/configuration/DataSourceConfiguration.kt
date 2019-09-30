package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost/szolnok_app?serverTimezone=Europe/Budapest")
                .username("root")
                .password("root")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build()

    }

    @Bean
    @Primary
    @Qualifier("embeddedDataSource")
    fun secondaryDataSource(): DataSource {
        return EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
    }
}