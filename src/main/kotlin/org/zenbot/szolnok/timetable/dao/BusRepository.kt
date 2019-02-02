package org.zenbot.szolnok.timetable.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.domain.Bus
import java.util.Optional

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Optional<Bus>
}
