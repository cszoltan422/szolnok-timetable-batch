package org.zenbot.szolnok.timetable.backend.api.bus

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/bus/")
class BusController(private val busService: BusService) {

    private val log = LoggerFactory.getLogger(BusController::class.java)

    @GetMapping()
    fun getBuses(@RequestParam("q") query: String?) : List<BusResponse> {
        log.info("/api/bus/?q=$query ")
        return busService.getBuses(query)
    }
}