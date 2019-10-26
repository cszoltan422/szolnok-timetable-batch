package org.zenbot.szolnok.timetable.backend.domain.entity.job

import org.hibernate.annotations.GenericGenerator
import org.springframework.batch.core.BatchStatus
import java.time.LocalDateTime
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
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

    @ElementCollection
    @CollectionTable(
            name = "batch_job_parameters",
            schema = "szolnok_app",
            joinColumns = arrayOf(JoinColumn(name = "batch_job_id"))
    )
    @Column(name = "parameter")
    var parameters: Set<String> = HashSet(),

    @Column(name = "finished")
    var finished: Boolean = false,

    @Column(name = "promoted")
    var promotedToProd: Boolean = false,

    @Column(name = "promotable")
    var promotable: Boolean = true
)
