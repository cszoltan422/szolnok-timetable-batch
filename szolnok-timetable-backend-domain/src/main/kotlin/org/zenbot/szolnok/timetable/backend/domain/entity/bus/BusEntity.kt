package org.zenbot.szolnok.timetable.backend.domain.entity.bus

import org.hibernate.annotations.GenericGenerator
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "bus")
data class BusEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "bus_name")
    var busName: String = "",

    @OneToMany(cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE))
    @JoinColumn(name = "bus_id")
    var busRouteEntities: MutableList<BusRouteEntity> = ArrayList(),

    @Column(name = "target_state")
    @Enumerated(value = EnumType.STRING)
    var targetState: TargetState = TargetState.BATCH,

    @ManyToOne(cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE))
    @JoinColumn(name = "batch_job_id")
    var batchJobEntity: BatchJobEntity? = null
) {

    fun getBusRouteByStartStopName(startBusStopName: String): BusRouteEntity = this.busRouteEntities.stream()
                .filter { line -> line.startBusStop == startBusStopName }
                .findFirst()
                .orElse(BusRouteEntity())

    fun hasNoBusRoute(busRouteEntity: BusRouteEntity): Boolean = this.busRouteEntities.stream()
            .map { line -> line.startBusStop }
            .filter { startStop -> busRouteEntity.startBusStop == startStop }
            .count() == 0L
}
