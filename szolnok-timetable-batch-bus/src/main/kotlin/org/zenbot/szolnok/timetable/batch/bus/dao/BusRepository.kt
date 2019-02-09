package org.zenbot.szolnok.timetable.batch.bus.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.batch.bus.domain.Bus
import java.util.*

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Optional<Bus>
}
