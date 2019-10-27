package org.zenbot.szolnok.timetable.backend.api.stops

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.zenbot.szolnok.timetable.backend.domain.api.stops.BusStopsResponse
import org.zenbot.szolnok.timetable.backend.service.stops.BusStopsService

@RestController
class BusStopsController(
    private val busStopsService: BusStopsService
) {

    @RequestMapping(
            value = arrayOf("/api/stops/{bus}"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.GET)
    )
    fun getBusStopsOfBus(@PathVariable("bus") bus: String): BusStopsResponse =
            busStopsService.findAllBusStopsOfBus(bus)

    @RequestMapping(
            value = arrayOf("/api/stops/{bus}/{startBusStop}"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.GET))
    fun getBusStopsOfBus(
        @PathVariable("bus") bus: String,
        @PathVariable("startBusStop") startBusStop: String
    ): BusStopsResponse =
            busStopsService.findAllBusStopsOfBus(bus, startBusStop)
}
