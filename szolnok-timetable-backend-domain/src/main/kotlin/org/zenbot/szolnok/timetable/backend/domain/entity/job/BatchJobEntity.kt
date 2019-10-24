package org.zenbot.szolnok.timetable.backend.domain.entity.job

import org.hibernate.annotations.GenericGenerator
import org.springframework.batch.core.BatchStatus
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "batch_job")
data class BatchJobEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "start_time")
    var startTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "finish_time")
    var finishTime: LocalDateTime? = null,

    @Column(name = "type")
    var type: String = "",

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    var status: BatchStatus = BatchStatus.UNKNOWN,

    @Column(name = "parameters")
    var parameters: String = "",

    @Column(name = "finished")
    var finished: Boolean = false
)
