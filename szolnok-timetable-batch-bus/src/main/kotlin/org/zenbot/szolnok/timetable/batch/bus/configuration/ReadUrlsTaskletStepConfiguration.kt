package org.zenbot.szolnok.timetable.batch.bus.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.utils.common.batch.tasklet.ReadFileResourcesTasklet

@Configuration
class ReadUrlsTaskletStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val readFileResourcesTasklet: ReadFileResourcesTasklet
) {

    @Bean
    open fun readUrlsStep(): Step {
        return stepBuilderFactory.get("readUrlsStep")
                .tasklet(readFileResourcesTasklet)
                .build()
    }
}
