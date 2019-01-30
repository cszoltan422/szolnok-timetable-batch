package org.zenbot.szolnok.timetable.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.domain.BusStopWithBuses

import java.util.Optional

interface BusStopRepository : MongoRepository<BusStopWithBuses, String> {

    fun findByBusStopName(s: String): Optional<BusStopWithBuses>
}
