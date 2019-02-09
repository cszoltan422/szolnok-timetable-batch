package org.zenbot.szolnok.timetable.batch.utils.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("timetable")
data class TimetableProperties(
        var resource: TimetableResourceProperties = TimetableResourceProperties(),
        var selector: TimetableSelectorProperties = TimetableSelectorProperties()
)
