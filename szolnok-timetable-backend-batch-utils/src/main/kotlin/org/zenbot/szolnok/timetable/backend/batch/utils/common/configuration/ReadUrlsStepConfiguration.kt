package org.zenbot.szolnok.timetable.backend.batch.utils.common.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet.ReadUrlResourcesTasklet

@Configuration
class ReadUrlsStepConfiguration(
        private val stepBuilderFactory: StepBuilderFactory,
        private val readUrlResourcesTasklet: ReadUrlResourcesTasklet
) {

    @Bean
    fun readUrlsStep(): Step {
        return stepBuilderFactory.get("readUrlsStep")
                .tasklet(readUrlResourcesTasklet)
                .build()
    }
}