package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.stop.BusStopWithBusesEntity
import java.util.Optional

@Repository
interface BusStopRepository : CrudRepository<BusStopWithBusesEntity, Long> {

    fun findByBusStopName(s: String): Optional<BusStopWithBusesEntity>
}