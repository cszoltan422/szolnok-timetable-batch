package org.zenbot.szolnok.timetable.backend.service.stops

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.BDDMockito.verifyNoMoreInteractions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.domain.api.stops.BusStopsResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.BusRouteEntity
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.repository.BusRepository

class BusStopsServiceTest {

    @InjectMocks
    private lateinit var testSubject: BusStopsService

    @Mock
    private lateinit var busRepository: BusRepository

    @Mock
    private lateinit var busStopsTransformer: BusStopsTransformer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `findAllBusStopsOfBus with no startStop should return an empty response if bus is not present`() {
        // GIVEN

        val empty = BusStopsResponse(
                busName = "",
                startStop = "",
                endStop = "",
                numberOfRoutes = 0,
                busStops = emptyList(),
                found = false
        )

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.PRODUCTION))
                .willReturn(null)
        given(busStopsTransformer.empty()).willReturn(empty)

        // WHEN
        val result = testSubject.findAllBusStopsOfBus("busName")

        // THEN
        assertThat(result)
                .isNotNull()
                .isSameAs(empty)

        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.PRODUCTION)
        verify(busStopsTransformer).empty()
        verifyNoMoreInteractions(busStopsTransformer)
    }

    @Test
    fun `findAllBusStopsOfBus should return with the transformed entity`() {
        // GIVEN

        val transformed = BusStopsResponse(
                busName = "",
                startStop = "",
                endStop = "",
                numberOfRoutes = 0,
                busStops = emptyList(),
                found = false
        )
        val busRouteEntity1 = BusRouteEntity()
        val busRouteEntity2 = BusRouteEntity()
        val busEntity = BusEntity(
                busRouteEntities = mutableListOf(
                        busRouteEntity1,
                        busRouteEntity2
                )
        )

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.PRODUCTION))
                .willReturn(busEntity)
        given(busStopsTransformer.transform(busEntity, busRouteEntity1)).willReturn(transformed)

        // WHEN
        val result = testSubject.findAllBusStopsOfBus("busName")

        // THEN
        assertThat(result)
                .isNotNull()
                .isSameAs(transformed)

        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.PRODUCTION)
        verify(busStopsTransformer).transform(busEntity, busRouteEntity1)
        verifyNoMoreInteractions(busStopsTransformer)
    }

    @Test
    fun `findAllBusStopsOfBus should return empty response if bus is not present`() {
        // GIVEN

        val empty = BusStopsResponse(
                busName = "",
                startStop = "",
                endStop = "",
                numberOfRoutes = 0,
                busStops = emptyList(),
                found = false
        )

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.PRODUCTION))
                .willReturn(null)
        given(busStopsTransformer.empty()).willReturn(empty)

        // WHEN
        val result = testSubject.findAllBusStopsOfBus("busName", "startBusStop")

        // THEN
        assertThat(result)
                .isNotNull()
                .isSameAs(empty)

        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.PRODUCTION)
        verify(busStopsTransformer).empty()
        verifyNoMoreInteractions(busStopsTransformer)
    }

    @Test
    fun `findAllBusStopsOfBus should return empty response if bus has no route starting with the supplied startStop`() {
        // GIVEN

        val empty = BusStopsResponse(
                busName = "",
                startStop = "",
                endStop = "",
                numberOfRoutes = 0,
                busStops = emptyList(),
                found = false
        )
        val busRouteEntity1 = BusRouteEntity(startBusStop = "startBusStop1")
        val busRouteEntity2 = BusRouteEntity(startBusStop = "startBusStop2")
        val busEntity = BusEntity(
                busRouteEntities = mutableListOf(
                        busRouteEntity1,
                        busRouteEntity2
                )
        )

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.PRODUCTION)).willReturn(busEntity)
        given(busStopsTransformer.empty()).willReturn(empty)

        // WHEN
        val result = testSubject.findAllBusStopsOfBus("busName", "startBusStop")

        // THEN
        assertThat(result)
                .isNotNull()
                .isSameAs(empty)

        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.PRODUCTION)
        verify(busStopsTransformer).empty()
        verifyNoMoreInteractions(busStopsTransformer)
    }

    @Test
    fun `findAllBusStopsOfBus should return the transformed entity with the startStop`() {
        // GIVEN

        val transformed = BusStopsResponse(
                busName = "",
                startStop = "",
                endStop = "",
                numberOfRoutes = 0,
                busStops = emptyList(),
                found = false
        )
        val busRouteEntity1 = BusRouteEntity(startBusStop = "startBusStop1")
        val busRouteEntity2 = BusRouteEntity(startBusStop = "startBusStop")
        val busEntity = BusEntity(
                busRouteEntities = mutableListOf(
                        busRouteEntity1,
                        busRouteEntity2
                )
        )

        given(busRepository.findByBusNameAndTargetState("busName", TargetState.PRODUCTION)).willReturn(busEntity)
        given(busStopsTransformer.transform(busEntity, busRouteEntity2)).willReturn(transformed)

        // WHEN
        val result = testSubject.findAllBusStopsOfBus("busName", "startBusStop")

        // THEN
        assertThat(result)
                .isNotNull()
                .isSameAs(transformed)

        verify(busRepository).findByBusNameAndTargetState("busName", TargetState.PRODUCTION)
        verify(busStopsTransformer).transform(busEntity, busRouteEntity2)
        verifyNoMoreInteractions(busStopsTransformer)
    }
}
