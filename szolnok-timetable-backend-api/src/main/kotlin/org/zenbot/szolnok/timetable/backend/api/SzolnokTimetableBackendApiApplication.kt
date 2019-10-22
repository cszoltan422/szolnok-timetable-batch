package org.zenbot.szolnok.timetable.backend.api

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SzolnokTimetableBackendApiApplication(
    private val szolnokBusesJob: Job,
    private val jobLauncher: JobLauncher
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        jobLauncher.run(szolnokBusesJob, JobParameters())
    }
}

fun main(args: Array<String>) {
    runApplication<SzolnokTimetableBackendApiApplication>(*args)
}