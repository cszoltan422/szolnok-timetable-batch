package org.zenbot.szolnok.timetable.backend.api.timetable

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TimetableController(
        private val timetableService: TimetableService
) {

    @RequestMapping(value = arrayOf("/api/timetable/{bus}/{startBusStop}/{busStop}"), produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getTimetable(@PathVariable("bus") bus : String,
                     @PathVariable("startBusStop") startBusStop : String,
                     @PathVariable("busStop") busStop : String,
                     @RequestParam(value = "occurrence", required = false) occurrence : Int? ) : TimetableResponse =
            timetableService.getTimetable(bus, startBusStop, busStop, occurrence ?: 1)
}