package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity

@Repository
interface BusRepository : JpaRepository<BusEntity, Long> {

    fun findByBusName(busName: String): BusEntity?

    fun findAllByBusNameContains(query : String): List<BusEntity>
}
