package org.zenbot.szolnok.timetable.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.step.readurls.ReadFileResourcesTasklet
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties

@Configuration
@EnableConfigurationProperties(TimetableProperties::class)
open class ReadUrlsTaskletStepConfiguration(private val stepBuilderFactory: StepBuilderFactory, private val readFileResourcesTasklet: ReadFileResourcesTasklet) {

    @Bean
    open fun readUrlsStep(): Step {
        return stepBuilderFactory.get("readUrlsStep")
                .tasklet(readFileResourcesTasklet)
                .build()
    }
}
