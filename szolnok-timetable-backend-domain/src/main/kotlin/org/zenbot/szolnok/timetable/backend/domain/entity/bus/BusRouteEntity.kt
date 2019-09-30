package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import javax.persistence.*

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