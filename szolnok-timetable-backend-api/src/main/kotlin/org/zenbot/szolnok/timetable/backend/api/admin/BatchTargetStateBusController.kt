package org.zenbot.szolnok.timetable.backend.api.admin

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.service.bus.BusService

@RestController
class BatchTargetStateBusController(
        private val busService: BusService
) {
    @RequestMapping(
            value = arrayOf("/admin/api/bus/"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.GET)
    )
    fun getAllBuses(): List<BusResponse> = busService.findAllByTargetState("", TargetState.BATCH)
}