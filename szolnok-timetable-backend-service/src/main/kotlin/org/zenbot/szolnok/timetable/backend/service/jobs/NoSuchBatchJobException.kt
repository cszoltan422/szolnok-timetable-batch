package org.zenbot.szolnok.timetable.backend.service.jobs

import java.lang.RuntimeException

class NoSuchBatchJobException(
    override val message: String
) : RuntimeException(message)
