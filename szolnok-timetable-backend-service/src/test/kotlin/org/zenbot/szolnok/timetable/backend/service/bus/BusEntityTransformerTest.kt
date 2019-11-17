package org.zenbot.szolnok.timetable.backend.service.bus

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.job.BatchJobEntity

class BusEntityTransformerTest {

    private lateinit var testSubject: BusEntityTransformer

    @Before
    fun setUp() {
        testSubject = BusEntityTransformer()
    }

    @Test
    fun `transform should create a response object and set the batchJobId to 0 if the bus has no batch job`() {
        // GIVEN
        val entity = BusEntity(
                busName = "busName",
                busRouteEntities = mutableListOf(BusRouteEntity(
                        startBusStop = "startBusStop",
                        endBusStop = "endBusStop"
                )),
                batchJobEntity = null

        )

        // WHEN
        val result = testSubject.transform(entity)

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("startBusStop", "startBusStop")
                .hasFieldOrPropertyWithValue("endBusStop", "endBusStop")
                .hasFieldOrPropertyWithValue("batchJobId", 0L)
    }

    @Test
    fun `transform should create a response object and set the batchJobId the batch job's id`() {
        // GIVEN
        val entity = BusEntity(
                busName = "busName",
                busRouteEntities = mutableListOf(BusRouteEntity(
                        startBusStop = "startBusStop",
                        endBusStop = "endBusStop"
                )),
                batchJobEntity = BatchJobEntity(
                        id = 10L
                )

        )

        // WHEN
        val result = testSubject.transform(entity)

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("startBusStop", "startBusStop")
                .hasFieldOrPropertyWithValue("endBusStop", "endBusStop")
                .hasFieldOrPropertyWithValue("batchJobId", 10L)
    }
}