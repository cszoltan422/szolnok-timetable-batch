package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.batch.core.BatchStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity

@Repository
interface BatchJobRepository : JpaRepository<BatchJobEntity, Long> {

    fun findTop5ByOrderByStartTimeDesc(): List<BatchJobEntity>

    fun findAllByTypeAndStatusAndFinishedFalse(type: String, status: BatchStatus): List<BatchJobEntity>

    fun findAllByTypeAndFinishedTrueAndPromotableTrue(type: String): List<BatchJobEntity>
}