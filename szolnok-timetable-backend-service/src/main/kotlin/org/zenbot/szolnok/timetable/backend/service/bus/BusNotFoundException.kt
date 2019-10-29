package org.zenbot.szolnok.timetable.backend.service.bus

import java.lang.RuntimeException

class BusNotFoundException(
        override val message: String
) : RuntimeException(message)
