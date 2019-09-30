package org.zenbot.szolnok.timetable.backend.domain.entity.stop

import javax.persistence.*

@Entity
@Table(schema = "szolnok_app", name = "bus_stop_buses")
data class BusStopWithBusesEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        var busStopName: String = "",
        @OneToMany(
                cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true,
                fetch = FetchType.EAGER)
        @JoinColumn(name = "bus_of_stop_id")
        var buses: MutableSet<BusOfBusStopEntity> = HashSet()
)