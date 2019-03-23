package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.backend.domain.document.stop.BusStopWithBuses
import java.util.Optional

interface BusStopRepository : MongoRepository<BusStopWithBuses, String> {

    fun findByBusStopName(s: String): Optional<BusStopWithBuses>
}