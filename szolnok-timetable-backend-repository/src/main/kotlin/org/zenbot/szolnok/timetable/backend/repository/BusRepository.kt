package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.backend.domain.document.bus.Bus

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Bus?
}
