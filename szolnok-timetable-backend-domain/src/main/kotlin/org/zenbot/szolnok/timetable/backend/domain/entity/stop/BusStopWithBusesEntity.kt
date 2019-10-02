package org.zenbot.szolnok.timetable.backend.domain.entity.stop

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
@Table(schema = "szolnok_app", name = "bus_stop_buses")
data class BusStopWithBusesEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "bus_stop_name")
    var busStopName: String = "",

    @OneToMany(
            cascade = arrayOf(CascadeType.ALL),
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_of_stop_id")
    var buses: MutableSet<BusOfBusStopEntity> = HashSet()
)