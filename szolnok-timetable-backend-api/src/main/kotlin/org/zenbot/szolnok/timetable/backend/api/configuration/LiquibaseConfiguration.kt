package org.zenbot.szolnok.timetable.backend.api.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class LiquibaseConfiguration(
    @Qualifier("dataSource")
    private val dataSource: DataSource
) {
    @Bean
    fun liquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = "classpath:liquibase/liquibase-changeLog.xml"
        liquibase.dataSource = dataSource
        return liquibase
    }
}
