package org.zenbot.szolnok.timetable.admin.commandline

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class Asd(
        private val jobLauncher : JobLauncher,
        @Qualifier("szolnokBusesJob")
        private val busJob : Job
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        jobLauncher.run(busJob, JobParameters())
    }
}