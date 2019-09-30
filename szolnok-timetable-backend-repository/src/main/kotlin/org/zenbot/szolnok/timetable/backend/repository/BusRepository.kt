package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity

@Repository
interface BusRepository : CrudRepository<BusEntity, Long> {

    fun findByBusName(busName: String): BusEntity?
}
