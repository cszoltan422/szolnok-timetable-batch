package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.batch.bus",
        "org.zenbot.szolnok.timetable.backend.batch.stops"))
class BatchJobsConfiguration