package org.zenbot.szolnok.timetable.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.domain.Bus

interface BusRepository : MongoRepository<Bus, String> {

    fun findByBusName(busName: String): Bus
}
