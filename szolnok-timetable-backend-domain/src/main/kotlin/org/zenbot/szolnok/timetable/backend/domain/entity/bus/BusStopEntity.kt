package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "bus_stop")
data class BusStopEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
@GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "bus_stop_name")
    var busStopName: String = "",

    @OneToOne(cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE))
    @JoinColumn(name = "workday_schedule_id")
    var workDayScheduleEntity: ScheduleEntity = ScheduleEntity(),

    @OneToOne(cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE))
    @JoinColumn(name = "saturday_schedule_id")
    var saturdayScheduleEntity: ScheduleEntity = ScheduleEntity(),

    @OneToOne(cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE))
    @JoinColumn(name = "sunday_schedule_id")
    var sundayScheduleEntity: ScheduleEntity = ScheduleEntity()
)