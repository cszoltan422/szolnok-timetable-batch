package org.zenbot.szolnok.timetable.backend.batch.news.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.elements.GetNewsJsoupElementsTasklet

@Configuration
class GetNewsJsoupElementsStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val getNewsJsoupElementsTasklet: GetNewsJsoupElementsTasklet
) {

    @Bean
    fun getNewsJsoupElementsStep(): Step {
        return stepBuilderFactory.get("getNewsJsoupElementsStep")
                .tasklet(getNewsJsoupElementsTasklet)
                .build()
    }
}