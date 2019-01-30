package org.zenbot.szolnok.timetable

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SzolnokTimetableBatchApplication

fun main(args: Array<String>) {
    runApplication<SzolnokTimetableBatchApplication>(*args)
}
