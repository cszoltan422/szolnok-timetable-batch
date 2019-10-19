package org.zenbot.szolnok.timetable.backend.api.bus

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BusController(
    private val busService: BusService
) {

    @RequestMapping(value = arrayOf("/api/bus"), produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getAllBuses(@RequestParam("q") q: String?): List<BusResponse> = busService.findAll(q ?: "")
}