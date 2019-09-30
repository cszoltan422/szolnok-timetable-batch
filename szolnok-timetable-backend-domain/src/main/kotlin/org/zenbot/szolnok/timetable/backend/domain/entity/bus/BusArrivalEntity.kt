package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import javax.persistence.*

@Entity
@Table(schema = "szolnok_app", name = "bus_arrival")
data class BusArrivalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var isLowfloor: Boolean = false,
    var arrivalHour: Int = 0,
    var arrivalMinute: Int? = null
)