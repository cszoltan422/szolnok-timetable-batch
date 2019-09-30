package org.zenbot.szolnok.timetable.backend.domain.entity.stop

import javax.persistence.*

@Entity
@Table(schema = "szolnok_app", name = "bus_of_stop")
data class BusOfBusStopEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,
        var name: String = ""
)