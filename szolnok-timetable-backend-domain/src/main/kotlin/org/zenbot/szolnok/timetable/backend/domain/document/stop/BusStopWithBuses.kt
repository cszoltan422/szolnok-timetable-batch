package org.zenbot.szolnok.timetable.backend.domain.document.stop

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class BusStopWithBuses(
    @Id var id: String? = null,
    var busStopName: String = "",
    var buses: MutableSet<String> = HashSet()
)