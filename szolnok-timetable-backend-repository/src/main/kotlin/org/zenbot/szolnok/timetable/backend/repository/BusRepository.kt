package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus
import java.util.Optional

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Optional<Bus>

    @Query(fields = "{busName: 1, busRoutes.startBusStop: 1, busRoutes.endBusStop: 1}")
    fun findAllByBusNameLikeOrderByBusName(busName: String): List<Bus>

    @Query(fields = "{busName: 1, busRoutes.startBusStop: 1, busRoutes.endBusStop: 1}")
    fun findBusesByBusNameNotNull() : List<Bus>
}