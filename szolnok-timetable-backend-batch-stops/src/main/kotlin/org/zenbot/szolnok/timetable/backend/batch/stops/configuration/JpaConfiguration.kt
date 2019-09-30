package org.zenbot.szolnok.timetable.backend.batch.stops.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.repository"))
class JpaConfiguration