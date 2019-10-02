package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "bus_schedule")
data class ScheduleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @OneToMany(
            cascade = arrayOf(CascadeType.ALL),
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_schedule_id")
    var busArrivalEntities: MutableList<BusArrivalEntity> = ArrayList()
)
