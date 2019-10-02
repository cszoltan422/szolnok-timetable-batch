package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Column
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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "start_bus_stop")
    var startBusStop: String = "",

    @Column(name = "end_bus_stop")
    var endBusStop: String = "",
    @OneToMany(cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST), fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_route_id")
    var busStopEntities: MutableList<BusStopEntity> = ArrayList()
) {

    fun addBusStopTimetable(busStopEntity: BusStopEntity) {
        busStopEntities.add(busStopEntity)
    }
}