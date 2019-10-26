package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity

@Repository
interface BusRepository : JpaRepository<BusEntity, Long> {

    fun findByBusNameAndTargetState(busName: String, targetState: TargetState): BusEntity?

    fun findAllByBatchJobEntityAndTargetState(batchJobEntity: BatchJobEntity, targetState: TargetState): List<BusEntity>

    fun findAllByBusNameContainsAndTargetState(query: String, targetState: TargetState): List<BusEntity>

    fun findAllByTargetState(targetState: TargetState): List<BusEntity>

    fun deleteAllByTargetState(targetState: TargetState)

}
