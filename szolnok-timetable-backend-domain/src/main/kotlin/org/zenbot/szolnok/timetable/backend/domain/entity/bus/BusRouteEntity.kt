package org.zenbot.szolnok.timetable.backend.domain.entity.bus

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
@Table(schema = "szolnok_app", name = "bus_route")
data class BusRouteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var startBusStop: String = "",
    var endBusStop: String = "",
    @OneToMany(
            cascade = arrayOf(CascadeType.ALL),
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_stop_id")
    var busStopEntities: MutableList<BusStopEntity> = ArrayList()
) {

    fun addBusStopTimetable(busStopEntity: BusStopEntity) {
        busStopEntities.add(busStopEntity)
    }
}