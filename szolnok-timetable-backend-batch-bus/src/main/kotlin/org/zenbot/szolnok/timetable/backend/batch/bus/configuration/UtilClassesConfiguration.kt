package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.UrlResourceToDocumentJsoupProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.reader.UrlResourceItemReader
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet.ReadFileResourcesTasklet
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

@Configuration
@EnableConfigurationProperties(TimetableProperties::class)
class UtilClassesConfiguration(
    private val properties: TimetableProperties
) {

    @Bean
    fun jsoupProcessor(): UrlResourceToDocumentJsoupProcessor {
        return UrlResourceToDocumentJsoupProcessor(jsoupDocumentService())
    }

    @Bean
    fun stringCleaner(): StringCleaner {
        return StringCleaner()
    }

    @Bean
    fun readFileResourcesTasklet(): ReadFileResourcesTasklet {
        return ReadFileResourcesTasklet(stringResourcesInMemoryStorage(), jsoupDocumentService(), properties)
    }

    @Bean
    fun jsoupDocumentService(): JsoupDocumentService {
        return JsoupDocumentService()
    }

    @Bean
    fun stringResourcesInMemoryStorage(): StringResourcesInMemoryStorage {
        return StringResourcesInMemoryStorage()
    }

    @Bean
    fun urlResourceItemReader(): UrlResourceItemReader {
        return UrlResourceItemReader(stringResourcesInMemoryStorage())
    }
}