package org.zenbot.szolnok.timetable.batch.bus.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.batch.bus.domain.BusStopWithBuses
import java.util.*

interface BusStopRepository : MongoRepository<BusStopWithBuses, String> {

    fun findByBusStopName(s: String): Optional<BusStopWithBuses>
}
