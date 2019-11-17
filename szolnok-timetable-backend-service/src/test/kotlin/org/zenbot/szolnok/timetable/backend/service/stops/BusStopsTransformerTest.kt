package org.zenbot.szolnok.timetable.backend.service.stops

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusStopEntity

class BusStopsTransformerTest {

    private lateinit var testSubject: BusStopsTransformer

    @Before
    fun setUp() {
        testSubject = BusStopsTransformer()
    }

    @Test
    fun `empty should return an empty result`() {
        // GIVEN

        // WHEN
        val result = testSubject.empty()

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "")
                .hasFieldOrPropertyWithValue("startStop", "")
                .hasFieldOrPropertyWithValue("endStop", "")
                .hasFieldOrPropertyWithValue("numberOfRoutes", 0)
                .hasFieldOrPropertyWithValue("busStops", emptyList<String>())
                .hasFieldOrPropertyWithValue("found", false)
    }

    @Test
    fun `transform should transform the entity into a response object`() {
        // GIVEN
        val route = BusRouteEntity(
                startBusStop = "startBusStop",
                endBusStop = "endBusStop",
                busStopEntities = mutableListOf(
                        BusStopEntity(busStopName = "busStopName1"),
                        BusStopEntity(busStopName = "busStopName2")
                )
        )

        val bus = BusEntity(busName = "busName", busRouteEntities = mutableListOf(route, BusRouteEntity()))

        // WHEN
        val result = testSubject.transform(bus, route)

        // THEN
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("startStop", "startBusStop")
                .hasFieldOrPropertyWithValue("endStop", "endBusStop")
                .hasFieldOrPropertyWithValue("numberOfRoutes", 2)
                .hasFieldOrPropertyWithValue("busStops", listOf("busStopName1", "busStopName2"))
                .hasFieldOrPropertyWithValue("found", true)
    }
}