package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import javax.persistence.*

@Entity
@Table(schema = "szolnok_app", name = "bus_stop")
data class BusStopEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        var busStopName: String = "",
        @OneToOne(
                cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true)
        var workDayScheduleEntity: ScheduleEntity = ScheduleEntity(),
        @OneToOne(
                cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true)
        var saturdayScheduleEntity: ScheduleEntity = ScheduleEntity(),
        @OneToOne(
                cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true)
        var sundayScheduleEntity: ScheduleEntity = ScheduleEntity()
)