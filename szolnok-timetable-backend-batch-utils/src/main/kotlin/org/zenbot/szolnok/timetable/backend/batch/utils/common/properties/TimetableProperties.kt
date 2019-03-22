package org.zenbot.szolnok.timetable.backend.batch.utils.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("timetable")
data class TimetableProperties(
    var resource: TimetableResourceProperties = TimetableResourceProperties(),
    var selector: TimetableSelectorProperties = TimetableSelectorProperties()
)
