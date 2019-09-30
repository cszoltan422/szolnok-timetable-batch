package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import javax.persistence.*

@Entity
@Table(schema = "szolnok_app", name = "bus_schedule")
data class ScheduleEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        @OneToMany(
                cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true,
                fetch = FetchType.EAGER)
        @JoinColumn(name = "bus_arrival_id")
        var busArrivalEntities: MutableList<BusArrivalEntity> = ArrayList()
)
