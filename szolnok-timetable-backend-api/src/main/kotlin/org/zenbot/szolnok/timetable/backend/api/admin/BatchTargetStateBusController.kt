package org.zenbot.szolnok.timetable.backend.api.admin

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.zenbot.szolnok.timetable.backend.domain.api.bus.BusResponse
import org.zenbot.szolnok.timetable.backend.domain.entity.bus.TargetState
import org.zenbot.szolnok.timetable.backend.service.bus.BatchJobOfBusInProgressException
import org.zenbot.szolnok.timetable.backend.service.bus.BusNotFoundException
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

    @RequestMapping(
            value = arrayOf("/admin/api/bus/{bus}/remove"),
            produces = arrayOf("application/json"),
            method = arrayOf(RequestMethod.POST)
    )
    fun removeBusFromBatchTargetState(@PathVariable("bus") bus: String): ResponseEntity<String> {
        var response: ResponseEntity<String>
        try {
            busService.removeBusFromBatchTargetState(bus)
            response = ResponseEntity.ok("Successfully deleted!")
        } catch (e: BatchJobOfBusInProgressException) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.message)
        } catch (e: BusNotFoundException) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.message)
        }
        return response
    }
}