package org.zenbot.szolnok.timetable.batch.bus.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.utils.batch.step.readurls.ReadFileResourcesTasklet
import org.zenbot.szolnok.timetable.batch.utils.batch.step.readurls.StringResourcesInMemoryStorage
import org.zenbot.szolnok.timetable.batch.utils.configuration.properties.TimetableProperties
import org.zenbot.szolnok.timetable.batch.utils.service.JsoupDocumentService

@Configuration
@EnableConfigurationProperties(TimetableProperties::class)
open class ReadUrlsTaskletStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val properties: TimetableProperties
) {

    @Bean
    open fun readUrlsStep(): Step {
        return stepBuilderFactory.get("readUrlsStep")
                .tasklet(readFileResourcesTasklet())
                .build()
    }

    @Bean
    open fun readFileResourcesTasklet(): ReadFileResourcesTasklet {
        return ReadFileResourcesTasklet(stringResourcesInMemoryStorage(), jsoupDocumentService(), properties)
    }

    @Bean
    open fun jsoupDocumentService(): JsoupDocumentService {
        return JsoupDocumentService()
    }

    @Bean
    open fun stringResourcesInMemoryStorage(): StringResourcesInMemoryStorage {
        return StringResourcesInMemoryStorage()
    }
}
