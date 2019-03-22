package org.zenbot.szolnok.timetable.backend.batch.bus.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus
import java.util.Optional

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Optional<Bus>
}
