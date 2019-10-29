package org.zenbot.szolnok.timetable.backend.service.bus

import java.lang.RuntimeException

class BatchJobInProgressException(
        override val message: String
) : RuntimeException(message)