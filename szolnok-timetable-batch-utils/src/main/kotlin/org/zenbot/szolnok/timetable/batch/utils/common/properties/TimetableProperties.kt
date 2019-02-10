package org.zenbot.szolnok.timetable.batch.utils.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("timetable")
data class TimetableProperties(
    var resource: TimetableResourceProperties = TimetableResourceProperties(),
    var selector: TimetableSelectorProperties = TimetableSelectorProperties()
)
