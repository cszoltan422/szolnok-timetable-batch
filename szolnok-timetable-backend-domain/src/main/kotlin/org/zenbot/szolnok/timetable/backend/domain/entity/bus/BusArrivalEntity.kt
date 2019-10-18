package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "bus_arrival")
data class BusArrivalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "low_floor")
    var isLowfloor: Boolean = false,

    @Column(name = "arrival_hour")
    var arrivalHour: Int = 0,

    @Column(name = "arrival_minute")
    var arrivalMinute: Int? = 0
)
